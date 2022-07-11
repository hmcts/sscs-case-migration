package uk.gov.hmcts.reform.sscs.migration.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.client.RefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationRefDataService {

    private static final String SSCS_COURT_TYPE_ID = "31";

    private final IdamService idamService;
    private final RefDataApi refDataApi;

    public List<CourtVenue> retrieveCourtVenues() {
        IdamTokens idamTokens = idamService.getIdamTokens();

        if (idamTokens == null
            || (isBlank(idamTokens.getIdamOauth2Token())
                && isBlank(idamTokens.getServiceAuthorization()))) {
            throw new IllegalStateException("Unable to retrieve IdamTokens");
        }

        List<CourtVenue> courtVenues = refDataApi.courtVenueByName(
            idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(),
            SSCS_COURT_TYPE_ID);

        log.info("Retrieved {} court venues from reference data.", courtVenues.size());

        return courtVenues;
    }
}
