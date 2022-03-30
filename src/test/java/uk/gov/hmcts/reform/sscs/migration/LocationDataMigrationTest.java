package uk.gov.hmcts.reform.sscs.migration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;
import uk.gov.hmcts.reform.sscs.service.RefDataService;


public class LocationDataMigrationTest {

    @Mock
    private RefDataService refDataService;

    private LocationDataMigration locationDataMigration;

    @BeforeEach
    public void setup() {
        openMocks(this);
        locationDataMigration = new LocationDataMigration(refDataService);
        when(refDataService.getVenueRefData(anyString())).thenReturn(CourtVenue.builder().epimsId("epimms_id").regionId("region_id").build());
    }

    @Ignore
    @Test
    public void testRefData() {
        SscsCaseData caseData = SscsCaseData.builder().build();
        locationDataMigration.apply(caseData);
        Assertions.assertNull(caseData.getCaseManagementLocation());

        caseData.setProcessingVenue("Bradford");
        locationDataMigration.apply(caseData);
        Assertions.assertNotNull(caseData.getCaseManagementLocation());
        Assertions.assertEquals("epimms_id", caseData.getCaseManagementLocation().getBaseLocation());
        Assertions.assertEquals("region_id", caseData.getCaseManagementLocation().getRegion());
    }
}
