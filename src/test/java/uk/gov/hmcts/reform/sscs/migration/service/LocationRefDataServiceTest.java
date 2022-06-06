package uk.gov.hmcts.reform.sscs.migration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.sscs.client.RefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;

@RunWith(MockitoJUnitRunner.class)
class LocationRefDataServiceTest {

    private static final String SSCS_COURT_TYPE_ID = "31";

    @Mock
    private IdamService idamService;

    @Mock
    private RefDataApi refDataApi;

    @InjectMocks
    private LocationRefDataService locationRefDataService;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    void shouldReturnEmptyList_givenIdamTokensIsNull() {
        List<CourtVenue> courtVenues = locationRefDataService.retrieveCourtVenues();

        verifyNoInteractions(refDataApi);
        assertThat(courtVenues).isEmpty();
    }

    @Test
    void shouldReturnEmptyList_givenIdamTokensIsEmpty() {
        when(idamService.getIdamTokens()).thenReturn(IdamTokens.builder()
            .idamOauth2Token(StringUtils.EMPTY)
            .serviceAuthorization(StringUtils.EMPTY)
            .build());

        List<CourtVenue> courtVenues = locationRefDataService.retrieveCourtVenues();

        verifyNoInteractions(refDataApi);
        assertThat(courtVenues).isEmpty();
    }

    @Test
    void shouldCallRefDataApiAsExpected_givenValidIdamTokensIsPresent() {
        when(idamService.getIdamTokens()).thenReturn(IdamTokens.builder()
            .idamOauth2Token("idamOauth2Token")
            .serviceAuthorization("serviceAuthorization")
            .build());

        locationRefDataService.retrieveCourtVenues();

        verify(refDataApi, times(1)).courtVenueByName(
            "idamOauth2Token",
            "serviceAuthorization",
            SSCS_COURT_TYPE_ID);
    }

}