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
import uk.gov.hmcts.reform.sscs.exception.MigrationException;

@Service
@Slf4j
@ConditionalOnProperty(value = "migration.case_access_management.enabled", havingValue = "true")
public class CaseAccessManagementDataMigration implements DataMigrationStep {

    @Override
    public void apply(SscsCaseData sscsCaseData, Long id) {
        log.info("Applying case access management data migration steps for case: {} - Started", id);
        addCaseCategories(sscsCaseData, id);
        addCaseNames(sscsCaseData, id);
        setOgdTypeToDwp(sscsCaseData);
        log.info("Applying case access management data migration steps for case: {} - Completed", id);
    }

    private void addCaseCategories(SscsCaseData sscsCaseData, Long id) {
        Optional<Benefit> benefit = sscsCaseData.getBenefitType();
        benefit.ifPresentOrElse(value -> sscsCaseData.getCaseAccessManagementFields().setCategories(value), () -> {
            throw new MigrationException("Missing benefit type, unable to set case categories.");
        });

        log.info("  Case category set: {} for case: {}",
            sscsCaseData.getCaseAccessManagementFields().getCaseAccessCategory(), id);

    }

    private void addCaseNames(SscsCaseData sscsCaseData, Long id) {
        Optional.ofNullable(sscsCaseData.getAppeal())
            .map(Appeal::getAppellant)
            .map(Appellant::getName)
            .map(Name::getFullNameNoTitle)
            .ifPresentOrElse(name -> sscsCaseData.getCaseAccessManagementFields().setCaseNames(name), () -> {
                throw new MigrationException("Unable to set case names.");
            });

        log.info("  Case name set: {} for case: {}",
            sscsCaseData.getCaseAccessManagementFields().getCaseNameHmctsInternal(), id);
    }

    private void setOgdTypeToDwp(SscsCaseData sscsCaseData) {
        sscsCaseData.getCaseAccessManagementFields().setOgdType("DWP");
    }
}
