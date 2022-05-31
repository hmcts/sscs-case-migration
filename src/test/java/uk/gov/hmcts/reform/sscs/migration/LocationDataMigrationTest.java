package uk.gov.hmcts.reform.sscs.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;
import uk.gov.hmcts.reform.sscs.service.RefDataService;

class LocationDataMigrationTest {

    @Mock
    private RefDataService refDataService;

    @InjectMocks
    private LocationDataMigration locationDataMigration;

    @BeforeEach
    public void setup() {
        openMocks(this);
    }

    @Test
    void shouldSetCaseManagementLocationFieldsAsExpected_givenValidCaseData() {
        SscsCaseData caseData = SscsCaseData.builder()
            .processingVenue("Bradford")
            .build();

        when(refDataService.getVenueRefData("Bradford"))
            .thenReturn(CourtVenue.builder().epimsId("epimms_id").regionId("region_id").build());

        locationDataMigration.apply(caseData);

        assertNotNull(caseData.getCaseManagementLocation());
        assertEquals("epimms_id", caseData.getCaseManagementLocation().getBaseLocation());
        assertEquals("region_id", caseData.getCaseManagementLocation().getRegion());
    }

    @Test
    void shouldNotSetCaseManagementLocation_givenProcessingVenueIsBlank() {
        SscsCaseData caseData = SscsCaseData.builder()
            .processingVenue("")
            .build();

        locationDataMigration.apply(caseData);

        assertNull(caseData.getCaseManagementLocation());
    }

    @Test
    void shouldNotSetCaseManagementLocation_givenProcessingVenueIsNull() {
        SscsCaseData caseData = SscsCaseData.builder().build();

        locationDataMigration.apply(caseData);

        assertNull(caseData.getCaseManagementLocation());
    }
}
