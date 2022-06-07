package uk.gov.hmcts.reform.sscs.migration.helper;

import java.util.Optional;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appointee;

@Component
public class PostcodeResolver {

    public String resolvePostcode(@NonNull Appeal appeal) {
        return Optional.ofNullable(appeal.getAppellant())
            .map(Appellant::getAppointee)
            .map(Appointee::getAddress)
            .map(Address::getPostcode)
            .orElse(Optional.ofNullable(appeal.getAppellant())
                .map(Appellant::getAddress)
                .map(Address::getPostcode)
                .orElse(StringUtils.EMPTY));
    }
}
