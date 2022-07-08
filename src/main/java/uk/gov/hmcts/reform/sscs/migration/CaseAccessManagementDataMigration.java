package uk.gov.hmcts.reform.sscs.migration;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.Name;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;

@Service
@Slf4j
@ConditionalOnProperty(value = "migration.case_access_management.enabled", havingValue = "true")
public class CaseAccessManagementDataMigration implements DataMigrationStep {

    @Override
    public void apply(SscsCaseData sscsCaseData) {
        log.info("Applying case access management data migration steps for case: {} - Started ", sscsCaseData.getCaseReference());
        addCaseCategories(sscsCaseData);
        addCaseNames(sscsCaseData);
        setOgdTypeToDwp(sscsCaseData);
        log.info("Applying case access management data migration steps for case: {} - Completed", sscsCaseData.getCaseReference());
    }

    private void addCaseCategories(SscsCaseData sscsCaseData) {
        Optional<Benefit> benefit = sscsCaseData.getBenefitType();
        benefit.ifPresent(value -> sscsCaseData.getCaseAccessManagementFields().setCategories(value));
        log.info("  Case category set: {} ", sscsCaseData.getCaseAccessManagementFields().getCaseAccessCategory());

    }

    private void addCaseNames(SscsCaseData sscsCaseData) {
        Optional.ofNullable(sscsCaseData.getAppeal())
            .map(Appeal::getAppellant)
            .map(Appellant::getName)
            .map(Name::getFullNameNoTitle)
            .ifPresent(name -> sscsCaseData.getCaseAccessManagementFields()
                .setCaseNames(name));
        log.info("  Case name set: {} ", sscsCaseData.getCaseAccessManagementFields().getCaseNameHmctsInternal());
    }

    private void setOgdTypeToDwp(SscsCaseData sscsCaseData) {
        sscsCaseData.getCaseAccessManagementFields().setOgdType("DWP");
    }
}
