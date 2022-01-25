package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService;
import uk.gov.hmcts.reform.sscs.migration.CaseAccessManagementDataMigration;
import uk.gov.hmcts.reform.sscs.migration.LocationDataMigration;

public class SscsDataMigrationServiceTest {

    @Mock
    private CaseAccessManagementDataMigration caseAccessManagementDataMigration;

    @Mock
    private LocationDataMigration locationDataMigration;

    @Mock
    private SscsCcdConvertService sscsCcdConvertService;

    @Captor
    private ArgumentCaptor<SscsCaseData> capture;

    private SscsDataMigrationService sscsDataMigrationService;

    private SscsCaseData sscsCaseData;

    @BeforeEach
    public void setup() {
        openMocks(this);
        sscsDataMigrationService = new SscsDataMigrationService(sscsCcdConvertService, List.of(caseAccessManagementDataMigration, locationDataMigration));
        sscsCaseData = SscsCaseData.builder().ccdCaseId("id_1").build();
        when(sscsCcdConvertService.getCaseData(anyMap())).thenReturn(sscsCaseData);
    }

    @Test
    public void testExecutionOfMigrationSteps() {
        sscsDataMigrationService.migrate(Map.of());

        verify(caseAccessManagementDataMigration).apply(capture.capture());
        verify(locationDataMigration).apply(capture.capture());

        assertEquals(2, capture.getAllValues().size());
        assertEquals("id_1", capture.getAllValues().get(0).getCcdCaseId());
        assertEquals("id_1", capture.getAllValues().get(1).getCcdCaseId());
    }

    @Test
    public void accesptSscsCase() {
        assertFalse(sscsDataMigrationService.accepts().test(null));
        assertFalse(sscsDataMigrationService.accepts().test(CaseDetails.builder().jurisdiction("SSCS").build()));
        assertFalse(sscsDataMigrationService.accepts().test(CaseDetails.builder().data(Map.of()).jurisdiction("OTHER").build()));
        assertTrue(sscsDataMigrationService.accepts().test(CaseDetails.builder().data(Map.of()).jurisdiction("SSCS").build()));
    }
}