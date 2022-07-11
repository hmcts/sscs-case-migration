package uk.gov.hmcts.reform.sscs.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
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
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseAccessManagementFields;
import uk.gov.hmcts.reform.sscs.ccd.domain.Name;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;

@ExtendWith(MockitoExtension.class)
class CaseAccessManagementDataMigrationTest {

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
        when(caseDataMock.getAppeal()).thenReturn(Appeal.builder().build());
        when(caseDataMock.getCaseAccessManagementFields()).thenReturn(CaseAccessManagementFields.builder().build());
        caseAccessManagementDataMigration.apply(caseDataMock);

        assertNull(caseDataMock.getCaseAccessManagementFields().getCaseAccessCategory());
        assertNull(caseDataMock.getCaseAccessManagementFields().getCaseManagementCategory());
    }

    @Test
    void testCategoriesForGivenBenefitType() {
        caseAccessManagementDataMigration.apply(caseData);

        assertNotNull(caseData.getCaseAccessManagementFields());
        assertEquals("personalIndependencePayment", caseData.getCaseAccessManagementFields().getCaseAccessCategory());
        assertEquals(Benefit.PIP.getShortName(), caseData.getCaseAccessManagementFields().getCaseManagementCategory().getListItems().get(0).getCode());
        assertEquals(Benefit.PIP.getDescription(), caseData.getCaseAccessManagementFields().getCaseManagementCategory().getListItems().get(0).getLabel());
    }

    @Test
    void testCaseNameAppellant() {
        caseAccessManagementDataMigration.apply(caseData);

        assertEquals("First Last", caseData.getCaseAccessManagementFields().getCaseNameHmctsInternal());
        assertEquals("First Last", caseData.getCaseAccessManagementFields().getCaseNameHmctsRestricted());
        assertEquals("First Last", caseData.getCaseAccessManagementFields().getCaseNamePublic());
    }

    @Test
    void testOgdType() {
        caseAccessManagementDataMigration.apply(caseData);

        assertEquals("DWP", caseData.getCaseAccessManagementFields().getOgdType());
    }
}
