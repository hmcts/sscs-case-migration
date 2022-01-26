package uk.gov.hmcts.reform.sscs.migration;

import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;

public interface DataMigrationStep {

    void apply(SscsCaseData sscsCaseData);
}
