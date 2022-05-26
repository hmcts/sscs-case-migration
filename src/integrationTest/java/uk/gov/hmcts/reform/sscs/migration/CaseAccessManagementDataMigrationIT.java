package uk.gov.hmcts.reform.sscs.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.service.SscsDataMigrationServiceIT;

@TestPropertySource(properties = {
    "migration.case_access_management.enabled=true",
    "migration.startDate=2022-03-01",
    "migration.endDate=2022-03-30",
    "migration.dryrun=false"})
public class CaseAccessManagementDataMigrationIT extends SscsDataMigrationServiceIT {

    @Autowired
    private CaseAccessManagementDataMigration caseAccessManagementDataMigration;

    @Test
    public void verifyServiceCall() {
        assertNotNull(sscsCaseData.getCaseAccessManagementFields());
        assertEquals("personalIndependencePayment", sscsCaseData.getCaseAccessManagementFields().getCaseAccessCategory());
        assertEquals(Benefit.PIP.getShortName(), sscsCaseData.getCaseAccessManagementFields().getCaseManagementCategory().getListItems().get(0).getCode());
        assertEquals(Benefit.PIP.getDescription(), sscsCaseData.getCaseAccessManagementFields().getCaseManagementCategory().getListItems().get(0).getLabel());
        assertEquals("First Last", sscsCaseData.getCaseAccessManagementFields().getCaseNameHmctsInternal());
        assertEquals("First Last", sscsCaseData.getCaseAccessManagementFields().getCaseNameHmctsRestricted());
        assertEquals("First Last", sscsCaseData.getCaseAccessManagementFields().getCaseNamePublic());
        assertEquals("DWP", sscsCaseData.getCaseAccessManagementFields().getOgdType());
    }

}
