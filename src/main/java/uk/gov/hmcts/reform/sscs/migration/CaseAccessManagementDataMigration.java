package uk.gov.hmcts.reform.sscs.migration;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;

@Service
@Slf4j
@ConditionalOnProperty(value = "migration.case_access_management.enabled", havingValue = "true")
public class CaseAccessManagementDataMigration implements DataMigrationStep {

    @Override
    public void apply(SscsCaseData sscsCaseData) {
        log.info("Applying case access management data migration steps for case: {} ", sscsCaseData.getCaseReference());
        addCaseCategories(sscsCaseData);
        addCaseNames(sscsCaseData);
        setOgdTypeToDwp(sscsCaseData);
    }

    private void addCaseCategories(SscsCaseData sscsCaseData) {
        Optional<Benefit> benefit = sscsCaseData.getBenefitType();
        benefit.ifPresent(value -> sscsCaseData.getCaseAccessManagementFields().setCategories(value));
    }

    private void addCaseNames(SscsCaseData sscsCaseData) {
        final Appellant appellant = sscsCaseData.getAppeal().getAppellant();
        if (appellant != null
            && appellant.getName() != null) {
            sscsCaseData.getCaseAccessManagementFields()
                .setCaseNames(appellant.getName().getFullNameNoTitle());
        }

    }

    private void setOgdTypeToDwp(SscsCaseData sscsCaseData) {
        sscsCaseData.getCaseAccessManagementFields().setOgdType("DWP");
    }
}
