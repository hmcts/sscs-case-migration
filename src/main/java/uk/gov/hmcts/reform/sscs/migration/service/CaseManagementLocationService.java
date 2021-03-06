package uk.gov.hmcts.reform.sscs.migration.service;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseManagementLocation;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;
import uk.gov.hmcts.reform.sscs.service.RegionalProcessingCenterService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CaseManagementLocationService {

    private final CourtVenueService courtVenueService;

    private final RegionalProcessingCenterService regionalProcessingCenterService;

    public Optional<CaseManagementLocation> retrieveCaseManagementLocation(String processingVenue,
                                                                           String postcode) {
        if (isNotBlank(processingVenue)
            && isNotBlank(postcode)) {

            CourtVenue courtVenue = courtVenueService.lookupCourtVenueByName(processingVenue);
            RegionalProcessingCenter regionalProcessingCenter = regionalProcessingCenterService.getByPostcode(postcode);

            if (nonNull(courtVenue)
                && isNotBlank(courtVenue.getRegionId())
                && nonNull(regionalProcessingCenter)) {
                return Optional.of(CaseManagementLocation.builder()
                    .baseLocation(regionalProcessingCenter.getEpimsId())
                    .region(courtVenue.getRegionId())
                    .build());
            } else {
                log.error("  Unable to resolve court venue or RPC details: court venue: {}, regional processing centre: {}",
                    courtVenue, regionalProcessingCenter);
            }
        } else {
            log.error("  Unable to resolve case management location due to missing details. "
                + "Processing venue: {}, postcode: {}", processingVenue, postcode);
        }
        return Optional.empty();
    }

}
