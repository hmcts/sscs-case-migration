package uk.gov.hmcts.reform.sscs.migration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.service.SscsDataMigrationServiceIt;

@TestPropertySource(properties = { "migration.case_access_management.enabled=true" })
public class CaseAccessManagementDataMigrationIt extends SscsDataMigrationServiceIt {

    @Autowired
    private CaseAccessManagementDataMigration caseAccessManagementDataMigration;


    @Test
    public void verifyServiceCall() {
        Assertions.assertNotNull(sscsCaseData.getWorkAllocationFields());
        Assertions.assertEquals("personalIndependencePayment", sscsCaseData.getWorkAllocationFields().getCaseAccessCategory());
        Assertions.assertEquals(Benefit.PIP.getShortName(), sscsCaseData.getWorkAllocationFields().getCaseManagementCategory().getListItems().get(0).getCode());
        Assertions.assertEquals(Benefit.PIP.getDescription(), sscsCaseData.getWorkAllocationFields().getCaseManagementCategory().getListItems().get(0).getLabel());
        Assertions.assertEquals("First Last", sscsCaseData.getWorkAllocationFields().getCaseNameHmctsInternal());
        Assertions.assertEquals("First Last", sscsCaseData.getWorkAllocationFields().getCaseNameHmctsRestricted());
        Assertions.assertEquals("First Last", sscsCaseData.getWorkAllocationFields().getCaseNamePublic());
        Assertions.assertEquals("DWP", sscsCaseData.getWorkAllocationFields().getOgdType());
    }

}
