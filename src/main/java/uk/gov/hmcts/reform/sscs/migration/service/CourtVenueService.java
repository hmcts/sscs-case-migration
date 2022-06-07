package uk.gov.hmcts.reform.sscs.migration.service;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;

@Service
@RequiredArgsConstructor
public class CourtVenueService {

    private final LocationRefDataService locationRefDataService;
    private final Map<String, CourtVenue> courtVenuesByVenueName = Maps.newHashMap();

    @PostConstruct
    protected void init() {
        locationRefDataService.retrieveCourtVenues()
            .forEach(courtVenue -> courtVenuesByVenueName.put(
                courtVenue.getVenueName(),
                courtVenue));
    }

    public CourtVenue lookupCourtVenueByName(String venueName) {
        return courtVenuesByVenueName.get(venueName);
    }
}
