package uk.gov.hmcts.reform.sscs.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.sscs.service.SscsDataMigrationServiceIT;

@TestPropertySource(properties = {
    "migration.location_data.enabled=true",
    "migration.startDate=2022-03-01",
    "migration.endDate=2022-03-30",
    "migration.dryrun=false"})
public class LocationDataMigrationIT extends SscsDataMigrationServiceIT {

    @Autowired
    private LocationDataMigration locationDataMigration;

    @Test
    public void verifyServiceCall() {
        locationDataMigration.apply(sscsCaseData);
        assertNotNull(sscsCaseData.getCaseManagementLocation());
        assertEquals("8888", sscsCaseData.getCaseManagementLocation().getBaseLocation());
        assertEquals("10", sscsCaseData.getCaseManagementLocation().getRegion());
    }

}
