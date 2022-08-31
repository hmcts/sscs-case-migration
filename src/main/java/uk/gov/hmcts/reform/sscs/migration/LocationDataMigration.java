package uk.gov.hmcts.reform.sscs.migration;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseManagementLocation;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.exception.MigrationException;
import uk.gov.hmcts.reform.sscs.migration.helper.PostcodeResolver;
import uk.gov.hmcts.reform.sscs.migration.service.CaseManagementLocationService;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(value = "migration.location_data.enabled", havingValue = "true")
public class LocationDataMigration implements DataMigrationStep {

    private final PostcodeResolver postcodeResolver;
    private final CaseManagementLocationService caseManagementLocationService;

    @Override
    public void apply(SscsCaseData sscsCaseData, Long id) {
        log.info("Applying location data migration steps for case: {} - Started", id);
        addCaseManagementLocation(sscsCaseData, id);
        log.info("Applying location data migration steps for case: {} - Completed", id);
    }

    private void addCaseManagementLocation(SscsCaseData sscsCaseData, Long id) {
        String postcode = postcodeResolver.resolvePostcode(sscsCaseData.getAppeal());

        log.info("  Attempting to retrieve case management location for case: {}", id);

        Optional<CaseManagementLocation>
            locationOptional = caseManagementLocationService.retrieveCaseManagementLocation(postcode, sscsCaseData, id);

        if (locationOptional.isPresent()) {
            CaseManagementLocation location = locationOptional.get();

            sscsCaseData.setCaseManagementLocation(location);

            log.info("  Case management location fields for case {} set: Region: {}, Base location: {}.",
                id, location.getRegion(), location.getBaseLocation());
        } else {
            throw new MigrationException("Unable to resolve case management location for case: " + id);
        }
    }
}
