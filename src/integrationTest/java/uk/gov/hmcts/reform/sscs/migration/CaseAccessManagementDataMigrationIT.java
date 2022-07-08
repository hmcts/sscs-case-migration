package uk.gov.hmcts.reform.sscs.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.service.SscsDataMigrationServiceIT;

public class CaseAccessManagementDataMigrationIT extends SscsDataMigrationServiceIT {

    public static final String NAME = "First Last";
    @Autowired
    private CaseAccessManagementDataMigration caseAccessManagementDataMigration;

    @Test
    public void verifyServiceCall() {
        caseAccessManagementDataMigration.apply(sscsCaseData);
        assertNotNull(sscsCaseData.getCaseAccessManagementFields());
        assertEquals("personalIndependencePayment", sscsCaseData.getCaseAccessManagementFields().getCaseAccessCategory());
        assertEquals(Benefit.PIP.getShortName(), sscsCaseData.getCaseAccessManagementFields().getCaseManagementCategory().getListItems().get(0).getCode());
        assertEquals(Benefit.PIP.getDescription(), sscsCaseData.getCaseAccessManagementFields().getCaseManagementCategory().getListItems().get(0).getLabel());
        assertEquals(NAME, sscsCaseData.getCaseAccessManagementFields().getCaseNameHmctsInternal());
        assertEquals(NAME, sscsCaseData.getCaseAccessManagementFields().getCaseNameHmctsRestricted());
        assertEquals(NAME, sscsCaseData.getCaseAccessManagementFields().getCaseNamePublic());
        assertEquals("DWP", sscsCaseData.getCaseAccessManagementFields().getOgdType());
    }

}
