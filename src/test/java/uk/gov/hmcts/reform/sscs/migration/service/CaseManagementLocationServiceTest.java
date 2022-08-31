package uk.gov.hmcts.reform.sscs.migration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseManagementLocation;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;
import uk.gov.hmcts.reform.sscs.service.AirLookupService;
import uk.gov.hmcts.reform.sscs.service.RegionalProcessingCenterService;
import uk.gov.hmcts.reform.sscs.service.VenueService;

@ExtendWith(MockitoExtension.class)
class CaseManagementLocationServiceTest {

    public static final String BRADFORD = "Bradford";
    public static final String BRADFORD_POSTCODE = "BD1 1RX";
    public static final String VENUE_EPIMS_ID = "987123";
    private static final long CASE_ID = 1234L;
    private SscsCaseData caseData;
    @Mock
    private CourtVenueService courtVenueService;

    @Mock
    private RegionalProcessingCenterService regionalProcessingCenterService;

    @Mock
    private AirLookupService airLookupService;

    @Mock
    private VenueService venueService;

    @InjectMocks
    private CaseManagementLocationService caseManagementLocationService;

    @BeforeEach
    void setup() {
        caseData = SscsCaseData.builder()
            .appeal(Appeal.builder().benefitType(BenefitType.builder().code("UC").build()).build()).build();
    }

    @Test
    void shouldNotRetrieveCaseManagementLocation_givenBlankPostcode() {
        Optional<CaseManagementLocation> caseManagementLocation =
            caseManagementLocationService.retrieveCaseManagementLocation("TS1 1ST", caseData, CASE_ID);

        assertThat(caseManagementLocation).isEmpty();
    }

    @Test
    void shouldNotRetrieveCaseManagementLocation_givenNoProcessingVenue() {
        Optional<CaseManagementLocation> caseManagementLocation =
            caseManagementLocationService.retrieveCaseManagementLocation(BRADFORD_POSTCODE, caseData, CASE_ID);

        assertThat(caseManagementLocation).isEmpty();
    }

    @Test
    void shouldNotRetrieveCaseManagementLocation_givenNullRpc() {
        when(airLookupService.lookupAirVenueNameByPostCode(anyString(), any(BenefitType.class))).thenReturn(BRADFORD);

        Optional<CaseManagementLocation> caseManagementLocation =
            caseManagementLocationService.retrieveCaseManagementLocation(BRADFORD_POSTCODE, caseData, CASE_ID);

        assertThat(caseManagementLocation).isEmpty();
    }

    @Test
    void shouldNotRetrieveCaseManagementLocation_givenInvalidCourtVenue() {
        when(airLookupService.lookupAirVenueNameByPostCode(anyString(), any(BenefitType.class))).thenReturn(BRADFORD);
        when(venueService.getEpimsIdForVenue(BRADFORD)).thenReturn(VENUE_EPIMS_ID);
        when(courtVenueService.lookupCourtVenueByEpimsId(VENUE_EPIMS_ID)).thenReturn(CourtVenue.builder().build());

        RegionalProcessingCenter rpc = RegionalProcessingCenter.builder().build();
        Optional<CaseManagementLocation> caseManagementLocation =
            caseManagementLocationService.retrieveCaseManagementLocation(BRADFORD_POSTCODE, caseData, CASE_ID);

        assertThat(caseManagementLocation).isEmpty();
    }

    @Test
    void shouldNotRetrieveCaseManagementLocation_givenInvalidPostcode() {
        when(airLookupService.lookupAirVenueNameByPostCode(anyString(), any(BenefitType.class))).thenReturn(BRADFORD);
        when(venueService.getEpimsIdForVenue(BRADFORD)).thenReturn(VENUE_EPIMS_ID);
        when(courtVenueService.lookupCourtVenueByEpimsId(VENUE_EPIMS_ID)).thenReturn(CourtVenue.builder().regionId("regionId").build());

        Optional<CaseManagementLocation> caseManagementLocation =
            caseManagementLocationService.retrieveCaseManagementLocation(BRADFORD_POSTCODE, caseData, CASE_ID);

        assertThat(caseManagementLocation).isEmpty();
    }

    @Test
    void shouldRetrieveCaseManagementLocation_givenValidProcessingVenue_andPostcode() {
        when(airLookupService.lookupAirVenueNameByPostCode(anyString(), any(BenefitType.class))).thenReturn(BRADFORD);
        when(courtVenueService.lookupCourtVenueByEpimsId(VENUE_EPIMS_ID)).thenReturn(CourtVenue.builder().regionId("regionId").build());
        when(venueService.getEpimsIdForVenue(BRADFORD)).thenReturn(VENUE_EPIMS_ID);
        when(regionalProcessingCenterService.getByPostcode(BRADFORD_POSTCODE))
            .thenReturn(RegionalProcessingCenter.builder().epimsId(VENUE_EPIMS_ID).build());

        Optional<CaseManagementLocation> caseManagementLocation =
            caseManagementLocationService.retrieveCaseManagementLocation(BRADFORD_POSTCODE, caseData, CASE_ID);

        assertThat(caseManagementLocation).isPresent();
        CaseManagementLocation result = caseManagementLocation.get();
        assertThat(result.getBaseLocation()).isEqualTo(VENUE_EPIMS_ID);
        assertThat(result.getRegion()).isEqualTo("regionId");
    }

}
