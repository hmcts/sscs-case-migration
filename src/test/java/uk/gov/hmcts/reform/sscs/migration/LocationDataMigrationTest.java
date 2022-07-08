package uk.gov.hmcts.reform.sscs.migration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseManagementLocation;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.migration.helper.PostcodeResolver;
import uk.gov.hmcts.reform.sscs.migration.service.CaseManagementLocationService;

@ExtendWith(MockitoExtension.class)
class LocationDataMigrationTest {

    @Mock
    private PostcodeResolver postcodeResolver;

    @Mock
    private CaseManagementLocationService caseManagementLocationService;

    @InjectMocks
    private LocationDataMigration locationDataMigration;

    @Test
    void shouldSetCaseManagementLocationFieldsAsExpected_givenValidCaseData() {
        SscsCaseData caseData = SscsCaseData.builder()
            .processingVenue("Bradford")
            .appeal(Appeal.builder().build())
            .regionalProcessingCenter(RegionalProcessingCenter.builder().epimsId("rpcEpimsId").build())
            .build();

        when(postcodeResolver.resolvePostcode(Appeal.builder().build())).thenReturn("postcode");
        when(caseManagementLocationService.retrieveCaseManagementLocation("Bradford",
            "postcode"))
            .thenReturn(Optional.of(CaseManagementLocation.builder().baseLocation("rpcEpimsId").region("regionId").build()));

        locationDataMigration.apply(caseData);

        assertNotNull(caseData.getCaseManagementLocation());
        assertThat(caseData.getCaseManagementLocation().getBaseLocation()).isEqualTo("rpcEpimsId");
        assertThat(caseData.getCaseManagementLocation().getRegion()).isEqualTo("regionId");
    }
}
