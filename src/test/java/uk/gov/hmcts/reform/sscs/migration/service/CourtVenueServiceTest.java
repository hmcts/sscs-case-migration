package uk.gov.hmcts.reform.sscs.migration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;

@ExtendWith(MockitoExtension.class)
class CourtVenueServiceTest {

    public static final String EPIMS_ID = "1234";
    public static final String BRADFORD = "Bradford";
    public static final String BRADFORD_REGION_ID = "bradford-regionId";
    @Mock
    private LocationRefDataService locationRefDataService;

    @InjectMocks
    private CourtVenueService courtVenueService;

    @BeforeEach
    void setup() {
        when(locationRefDataService.retrieveCourtVenues()).thenReturn(List.of(CourtVenue.builder()
                .venueName(BRADFORD)
                .epimsId(EPIMS_ID)
                .regionId(BRADFORD_REGION_ID)
            .build()));
        courtVenueService.init();
    }

    @Test
    void shouldReturnCourtVenue_givenVenueIsPresent() {
        CourtVenue courtVenue = courtVenueService.lookupCourtVenueByEpimsId(EPIMS_ID);

        assertThat(courtVenue).isNotNull();
        assertThat(courtVenue.getRegionId()).isEqualTo(BRADFORD_REGION_ID);
    }

    @Test
    void shouldReturnNull_givenVenueIsMissing() {
        CourtVenue courtVenue = courtVenueService.lookupCourtVenueByEpimsId("4678392");

        assertThat(courtVenue).isNull();
    }

}
