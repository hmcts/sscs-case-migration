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

    @Mock
    private LocationRefDataService locationRefDataService;

    @InjectMocks
    private CourtVenueService courtVenueService;

    @BeforeEach
    void setup() {
        when(locationRefDataService.retrieveCourtVenues()).thenReturn(List.of(CourtVenue.builder()
                .venueName("Bradford")
                .regionId("bradford-regionId")
            .build()));
        courtVenueService.init();
    }

    @Test
    void shouldReturnCourtVenue_givenVenueIsPresent() {
        CourtVenue courtVenue = courtVenueService.lookupCourtVenueByName("Bradford");

        assertThat(courtVenue).isNotNull();
        assertThat(courtVenue.getRegionId()).isEqualTo("bradford-regionId");
    }

    @Test
    void shouldReturnNull_givenVenueIsMissing() {
        CourtVenue courtVenue = courtVenueService.lookupCourtVenueByName("Liverpool");

        assertThat(courtVenue).isNull();
    }

}
