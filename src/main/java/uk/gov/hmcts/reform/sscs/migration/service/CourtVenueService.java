package uk.gov.hmcts.reform.sscs.migration.service;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;

@Service
@RequiredArgsConstructor
public class CourtVenueService {

    private final LocationRefDataService locationRefDataService;
    private final Map<String, CourtVenue> courtVenuesByEpimsId = new HashMap<>();

    @PostConstruct
    protected void init() {
        locationRefDataService.retrieveCourtVenues()
            .forEach(courtVenue -> courtVenuesByEpimsId.put(
                courtVenue.getEpimsId(),
                courtVenue));
    }

    public CourtVenue lookupCourtVenueByEpimsId(String epimsId) {
        return courtVenuesByEpimsId.get(epimsId);
    }
}
