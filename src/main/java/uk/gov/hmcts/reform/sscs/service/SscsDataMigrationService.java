package uk.gov.hmcts.reform.sscs.service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.migration.service.DataMigrationService;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService;
import uk.gov.hmcts.reform.sscs.migration.DataMigrationStep;


@Service("dataMigrationService")
@Slf4j
public class SscsDataMigrationService implements DataMigrationService<SscsCaseData> {
    private static final String JURISDICTION_SSCS = "SSCS";

    private SscsCcdConvertService sscsCcdConvertService;

    private List<DataMigrationStep> dataMigrationSteps;

    @Autowired
    public SscsDataMigrationService(SscsCcdConvertService sscsCcdConvertService, List<DataMigrationStep> dataMigrationSteps) {
        this.sscsCcdConvertService = sscsCcdConvertService;
        this.dataMigrationSteps = dataMigrationSteps;
    }

    @Override
    public Predicate<CaseDetails> accepts() {
        return caseDetails -> caseDetails != null
            && caseDetails.getData() != null
            && JURISDICTION_SSCS.equalsIgnoreCase(caseDetails.getJurisdiction());
    }

    @Override
    public SscsCaseData migrate(Map<String, Object> data) {
        SscsCaseData sscsCaseData = sscsCcdConvertService.getCaseData(data);

        if (dataMigrationSteps != null && !dataMigrationSteps.isEmpty()) {
            dataMigrationSteps.forEach(s -> s.apply(sscsCaseData));
        }

        return sscsCaseData;
    }
}
