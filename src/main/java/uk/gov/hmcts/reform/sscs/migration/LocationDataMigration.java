package uk.gov.hmcts.reform.sscs.migration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.migration.helper.PostcodeResolver;
import uk.gov.hmcts.reform.sscs.migration.service.CaseManagementLocationService;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "migration.location_data.enabled", havingValue = "true")
public class LocationDataMigration implements DataMigrationStep {

    private final PostcodeResolver postcodeResolver;
    private final CaseManagementLocationService caseManagementLocationService;

    @Override
    public void apply(SscsCaseData sscsCaseData) {
        addCaseManagementLocation(sscsCaseData);
    }

    private void addCaseManagementLocation(SscsCaseData sscsCaseData) {
        String processingVenueName = sscsCaseData.getProcessingVenue();
        String postcode = postcodeResolver.resolvePostcode(sscsCaseData.getAppeal());

        caseManagementLocationService.retrieveCaseManagementLocation(processingVenueName, postcode)
            .ifPresent(sscsCaseData::setCaseManagementLocation);
    }
}
