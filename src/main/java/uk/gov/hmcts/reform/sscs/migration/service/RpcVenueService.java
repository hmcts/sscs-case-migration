package uk.gov.hmcts.reform.sscs.migration.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;
import uk.gov.hmcts.reform.sscs.service.RegionalProcessingCenterService;
import uk.gov.hmcts.reform.sscs.service.VenueService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RpcVenueService {

    private final RegionalProcessingCenterService regionalProcessingCenterService;
    private final VenueService venueService;

    public String retrieveRpcEpimsIdForPostcode(@NonNull String postcode) {
        RegionalProcessingCenter rpc = regionalProcessingCenterService.getByPostcode(postcode);

        return venueService.getEpimsIdForActiveVenueByPostcode(rpc.getPostcode())
            .orElseGet(() -> {
                log.error("Unable to retrieve venue epims id for RPC - {}", rpc.getName());
                return StringUtils.EMPTY;
            });
    }
}
