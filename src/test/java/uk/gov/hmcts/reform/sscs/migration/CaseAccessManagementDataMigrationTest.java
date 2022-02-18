package uk.gov.hmcts.reform.sscs.migration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;

@ExtendWith(MockitoExtension.class)
public class CaseAccessManagementDataMigrationTest {


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
    public void testCategoriesForEmptyBenefitType(@Mock SscsCaseData caseDataMock) {
        when(caseDataMock.getAppeal()).thenReturn(Appeal.builder().build());
        when(caseDataMock.getWorkAllocationFields()).thenReturn(WorkAllocationFields.builder().build());
        caseAccessManagementDataMigration.apply(caseDataMock);

        assertNull(caseDataMock.getWorkAllocationFields().getCaseAccessCategory());
        assertNull(caseDataMock.getWorkAllocationFields().getCaseManagementCategory());
    }

    @Test
    public void testCategoriesForGivenBenefitType() {
        caseAccessManagementDataMigration.apply(caseData);

        assertNotNull(caseData.getWorkAllocationFields());
        assertEquals("personalIndependencePayment", caseData.getWorkAllocationFields().getCaseAccessCategory());
        assertEquals(Benefit.PIP.getShortName(), caseData.getWorkAllocationFields().getCaseManagementCategory().getListItems().get(0).getCode());
        assertEquals(Benefit.PIP.getDescription(), caseData.getWorkAllocationFields().getCaseManagementCategory().getListItems().get(0).getLabel());
    }

    @Test
    public void testCaseNameAppellant() {
        caseAccessManagementDataMigration.apply(caseData);

        assertEquals("First Last", caseData.getWorkAllocationFields().getCaseNameHmctsInternal());
        assertEquals("First Last", caseData.getWorkAllocationFields().getCaseNameHmctsRestricted());
        assertEquals("First Last", caseData.getWorkAllocationFields().getCaseNamePublic());
    }

    @Test
    public void testOgdType() {
        caseAccessManagementDataMigration.apply(caseData);

        assertEquals("DWP", caseData.getWorkAllocationFields().getOgdType());
    }
}
