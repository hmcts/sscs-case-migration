package uk.gov.hmcts.reform.sscs.service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.migration.service.DataMigrationService;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService;
import uk.gov.hmcts.reform.sscs.migration.DataMigrationStep;

@Slf4j
@Service("dataMigrationService")
@RequiredArgsConstructor
public class SscsDataMigrationService implements DataMigrationService<SscsCaseData> {

    private static final String JURISDICTION = "SSCS";
    private final SscsCcdConvertService sscsCcdConvertService;
    private final List<DataMigrationStep> dataMigrationSteps;

    @Override
    public Predicate<CaseDetails> accepts() {
        return caseDetails ->
            caseDetails != null
            && caseDetails.getData() != null
            && JURISDICTION.equalsIgnoreCase(caseDetails.getJurisdiction());
    }

    @Override
    public SscsCaseData migrate(Map<String, Object> data, Long id) {
        SscsCaseData sscsCaseData = sscsCcdConvertService.getCaseData(data);

        if (dataMigrationSteps != null && !dataMigrationSteps.isEmpty()) {
            dataMigrationSteps
                .forEach(s -> s.apply(sscsCaseData, id));
        }

        return sscsCaseData;
    }
}
