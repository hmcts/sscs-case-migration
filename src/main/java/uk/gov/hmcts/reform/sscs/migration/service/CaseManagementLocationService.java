package uk.gov.hmcts.reform.sscs.migration.service;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseManagementLocation;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;

@Service
@RequiredArgsConstructor
public class CaseManagementLocationService {

    private final CourtVenueService courtVenueService;
    private final RpcVenueService rpcVenueService;

    public Optional<CaseManagementLocation> retrieveCaseManagementLocation(String processingVenue, String postcode) {
        if (isNotBlank(processingVenue)
            && isNotBlank(postcode)) {

            CourtVenue courtVenue = courtVenueService.lookupCourtVenueByName(processingVenue);
            String rpcEpimsId = rpcVenueService.retrieveRpcEpimsIdForPostcode(postcode);

            if (nonNull(courtVenue)
                && isNotBlank(courtVenue.getRegionId())
                && isNotBlank(rpcEpimsId)) {
                return Optional.of(CaseManagementLocation.builder()
                    .baseLocation(rpcEpimsId)
                    .region(courtVenue.getRegionId())
                    .build());
            }
        }
        return Optional.empty();
    }

}
