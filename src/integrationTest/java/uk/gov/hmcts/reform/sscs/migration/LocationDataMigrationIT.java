package uk.gov.hmcts.reform.sscs.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.reform.sscs.service.SscsDataMigrationServiceIT;

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
