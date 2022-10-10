package uk.gov.hmcts.reform.sscs.migration.service;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseManagementLocation;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.exception.MigrationException;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;
import uk.gov.hmcts.reform.sscs.service.AirLookupService;
import uk.gov.hmcts.reform.sscs.service.RegionalProcessingCenterService;
import uk.gov.hmcts.reform.sscs.service.VenueService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CaseManagementLocationService {

    private final CourtVenueService courtVenueService;

    private final RegionalProcessingCenterService regionalProcessingCenterService;

    private final AirLookupService airLookupService;

    private final VenueService venueService;

    public Optional<CaseManagementLocation> retrieveCaseManagementLocation(String postcode, SscsCaseData sscsCaseData,
                                                                           Long id) {
        if (isBlank(postcode)) {
            throw new MigrationException("Unable to resolve case management location due to missing postcode for case: " + id);
        }

        String processingVenue = airLookupService.lookupAirVenueNameByPostCode(postcode, sscsCaseData.getAppeal()
            .getBenefitType());

        if (processingVenue == null) {
            throw new MigrationException(String.format("Unable to resolve processing venue for postcode: %s, case: %d.",
                postcode, id));
        }

        String venueEpimsId = venueService.getEpimsIdForVenue(processingVenue);

        CourtVenue courtVenue = courtVenueService.lookupCourtVenueByEpimsId(venueEpimsId);
        RegionalProcessingCenter regionalProcessingCenter = regionalProcessingCenterService.getByPostcode(postcode);

        if (nonNull(courtVenue)
            && isNotBlank(courtVenue.getRegionId())
            && nonNull(regionalProcessingCenter)) {
            return Optional.of(CaseManagementLocation.builder()
                .baseLocation(regionalProcessingCenter.getEpimsId())
                .region(courtVenue.getRegionId())
                .build());
        } else {
            String error = String.format("Unable to resolve court venue or RPC details for case: %s: court venue: %s, regional processing centre: %s",
                id, courtVenue, regionalProcessingCenter);
            throw new MigrationException(error);
        }
    }

}
