package uk.gov.hmcts.reform.sscs.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.ccd.domain.Name;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.exception.MigrationException;

@ExtendWith(MockitoExtension.class)
class CaseAccessManagementDataMigrationTest {

    private static final long CASE_ID = 1L;
    private CaseAccessManagementDataMigration caseAccessManagementDataMigration;
    private SscsCaseData caseData;

    @BeforeEach
    public void setup() {
        openMocks(this);
        caseAccessManagementDataMigration = new CaseAccessManagementDataMigration();
        caseData = SscsCaseData.builder()
            .appeal(Appeal.builder()
                .benefitType(BenefitType.builder()
                    .code("PIP")
                    .build())
            .appellant(Appellant.builder()
               .name(Name.builder()
                 .firstName("First")
                 .lastName("Last")
                 .build())
               .build())
                .build())
            .build();
    }

    @Test
    void testCategoriesForEmptyBenefitType(@Mock SscsCaseData caseDataMock) {
        assertThrows(MigrationException.class, () -> caseAccessManagementDataMigration.apply(caseDataMock, CASE_ID));
    }

    @Test
    void testCategoriesForGivenBenefitType() {
        caseAccessManagementDataMigration.apply(caseData, CASE_ID);

        assertNotNull(caseData.getCaseAccessManagementFields());
        assertEquals("personalIndependencePayment", caseData.getCaseAccessManagementFields().getCaseAccessCategory());
        assertEquals(Benefit.PIP.getShortName(), caseData.getCaseAccessManagementFields().getCaseManagementCategory().getListItems().get(0).getCode());
        assertEquals(Benefit.PIP.getDescription(), caseData.getCaseAccessManagementFields().getCaseManagementCategory().getListItems().get(0).getLabel());
    }

    @Test
    void testCaseNameAppellant() {
        caseAccessManagementDataMigration.apply(caseData, CASE_ID);

        assertEquals("First Last", caseData.getCaseAccessManagementFields().getCaseNameHmctsInternal());
        assertEquals("First Last", caseData.getCaseAccessManagementFields().getCaseNameHmctsRestricted());
        assertEquals("First Last", caseData.getCaseAccessManagementFields().getCaseNamePublic());
    }

    @Test
    void testOgdType() {
        caseAccessManagementDataMigration.apply(caseData, CASE_ID);

        assertEquals("DWP", caseData.getCaseAccessManagementFields().getOgdType());
    }
}
