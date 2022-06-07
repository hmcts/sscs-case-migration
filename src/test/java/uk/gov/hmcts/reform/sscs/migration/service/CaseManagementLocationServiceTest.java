package uk.gov.hmcts.reform.sscs.migration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseManagementLocation;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;

@RunWith(MockitoJUnitRunner.class)
class CaseManagementLocationServiceTest {

    @Mock
    private CourtVenueService courtVenueService;

    @Mock
    private RpcVenueService rpcVenueService;

    @InjectMocks
    private CaseManagementLocationService caseManagementLocationService;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    void shouldNotRetrieveCaseManagementLocation_givenBlankProcessingVenue() {
        Optional<CaseManagementLocation> caseManagementLocation =
            caseManagementLocationService.retrieveCaseManagementLocation("", "postcode");

        assertThat(caseManagementLocation).isEmpty();
    }

    @Test
    void shouldNotRetrieveCaseManagementLocation_givenBlankPostcode() {
        Optional<CaseManagementLocation> caseManagementLocation =
            caseManagementLocationService.retrieveCaseManagementLocation("venue", "");

        assertThat(caseManagementLocation).isEmpty();
    }

    @Test
    void shouldNotRetrieveCaseManagementLocation_givenInvalidProcessingVenue() {
        when(courtVenueService.lookupCourtVenueByName("Bradford")).thenReturn(null);

        Optional<CaseManagementLocation> caseManagementLocation =
            caseManagementLocationService.retrieveCaseManagementLocation("Bradford", "BD1 1RX");

        assertThat(caseManagementLocation).isEmpty();
    }

    @Test
    void shouldNotRetrieveCaseManagementLocation_givenInvalidCourtVenue() {
        when(courtVenueService.lookupCourtVenueByName("Bradford")).thenReturn(CourtVenue.builder().build());

        Optional<CaseManagementLocation> caseManagementLocation =
            caseManagementLocationService.retrieveCaseManagementLocation("Bradford", "BD1 1RX");

        assertThat(caseManagementLocation).isEmpty();
    }

    @Test
    void shouldNotRetrieveCaseManagementLocation_givenInvalidPostcode() {
        when(courtVenueService.lookupCourtVenueByName("Bradford")).thenReturn(CourtVenue.builder().regionId("regionId").build());
        when(rpcVenueService.retrieveRpcEpimsIdForPostcode("BD1 1RX")).thenReturn(null);

        Optional<CaseManagementLocation> caseManagementLocation =
            caseManagementLocationService.retrieveCaseManagementLocation("Bradford", "BD1 1RX");

        assertThat(caseManagementLocation).isEmpty();
    }

    @Test
    void shouldRetrieveCaseManagementLocation_givenValidProcessingVenue_andPostcode() {
        when(courtVenueService.lookupCourtVenueByName("Bradford")).thenReturn(CourtVenue.builder().regionId("regionId").build());
        when(rpcVenueService.retrieveRpcEpimsIdForPostcode("BD1 1RX")).thenReturn("rpcEpimsId");

        Optional<CaseManagementLocation> caseManagementLocation =
            caseManagementLocationService.retrieveCaseManagementLocation("Bradford", "BD1 1RX");

        assertThat(caseManagementLocation).isPresent();
        CaseManagementLocation result = caseManagementLocation.get();
        assertThat(result.getBaseLocation()).isEqualTo("rpcEpimsId");
        assertThat(result.getRegion()).isEqualTo("regionId");
    }

}