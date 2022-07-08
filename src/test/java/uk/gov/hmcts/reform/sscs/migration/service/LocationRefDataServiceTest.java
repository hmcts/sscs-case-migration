package uk.gov.hmcts.reform.sscs.migration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.sscs.client.RefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;

@ExtendWith(MockitoExtension.class)
class LocationRefDataServiceTest {

    private static final String SSCS_COURT_TYPE_ID = "31";

    @Mock
    private IdamService idamService;

    @Mock
    private RefDataApi refDataApi;

    @InjectMocks
    private LocationRefDataService locationRefDataService;


    @ParameterizedTest
    @CsvSource(value = {",", "'',", ",''", "'',''"})
    void shouldThrowIllegalStateException_givenIdamTokensOrServiceAuthIsBlank(String idamToken, String serviceAuth) {
        when(idamService.getIdamTokens()).thenReturn(IdamTokens.builder()
            .idamOauth2Token(idamToken)
            .serviceAuthorization(serviceAuth)
            .build());

        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(locationRefDataService::retrieveCourtVenues);

    }

    @Test
    void shouldCallRefDataApiAsExpected_givenValidIdamTokensIsPresent() {
        String idamToken = "idamOauth2Token";
        String serviceAuth = "serviceAuthorization";
        CourtVenue venue = CourtVenue.builder().regionId("2").build();
        when(idamService.getIdamTokens()).thenReturn(IdamTokens.builder()
            .idamOauth2Token(idamToken)
            .serviceAuthorization(serviceAuth)
            .build());

        when(refDataApi.courtVenueByName(idamToken, serviceAuth, SSCS_COURT_TYPE_ID)).thenReturn(List.of(
            venue));
        List<CourtVenue> result =  locationRefDataService.retrieveCourtVenues();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(venue);

    }

}
