package uk.gov.hmcts.reform.sscs.migration;


import static org.apache.commons.lang3.StringUtils.isEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseManagementLocation;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;
import uk.gov.hmcts.reform.sscs.service.RefDataService;


@Service
@ConditionalOnProperty(value = "migration.location_data.enabled", havingValue = "true")
public class LocationDataMigration implements DataMigrationStep {

    private final RefDataService refDataService;

    @Autowired
    public LocationDataMigration(RefDataService refDataService) {
        this.refDataService = refDataService;
    }

    @Override
    public void apply(SscsCaseData sscsCaseData) {
        addCaseManagementLocation(sscsCaseData);
    }

    private void addCaseManagementLocation(SscsCaseData sscsCaseData) {
        String venue = sscsCaseData.getProcessingVenue();

        if (!isEmpty(venue)) {
            CourtVenue courtVenue = refDataService.getVenueRefData(venue);
            if (courtVenue != null) {
                sscsCaseData.setCaseManagementLocation(CaseManagementLocation.builder()
                    .baseLocation(courtVenue.getEpimsId())
                    .region(courtVenue.getRegionId()).build());
            }
        }
    }
}
