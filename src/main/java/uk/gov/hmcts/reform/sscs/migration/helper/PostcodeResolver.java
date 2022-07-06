package uk.gov.hmcts.reform.sscs.migration.helper;

import java.util.Optional;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appointee;
import uk.gov.hmcts.reform.sscs.ccd.domain.YesNo;

@Component
public class PostcodeResolver {

    public String resolvePostcode(@NonNull Appeal appeal) {
        Appellant appellant = appeal.getAppellant();
        String postcode;

        if (YesNo.isYes(appellant.getIsAppointee())) {
            postcode = Optional.ofNullable(appellant.getAppointee())
                .map(Appointee::getAddress)
                .map(Address::getPostcode)
                .filter(StringUtils::isEmpty)
                .orElse(StringUtils.EMPTY);
        } else {
            postcode = appellant.getAddress().getPostcode();
        }

        return postcode;
    }
}
