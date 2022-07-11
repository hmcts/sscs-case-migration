package uk.gov.hmcts.reform.sscs.migration.helper;

import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appointee;
import uk.gov.hmcts.reform.sscs.ccd.domain.YesNo;

@Component
@Slf4j
public class PostcodeResolver {

    public String resolvePostcode(@NonNull Appeal appeal) {
        Appellant appellant = appeal.getAppellant();
        String postcode;

        if (YesNo.isYes(appellant.getIsAppointee())) {
            postcode = Optional.ofNullable(appellant.getAppointee())
                .map(Appointee::getAddress)
                .map(Address::getPostcode)
                .orElse(StringUtils.EMPTY);
            log.info("  Resolved appointee postcode: {}.", postcode);
        } else {
            postcode = Optional.ofNullable(appellant.getAddress())
                .map(Address::getPostcode)
                .orElse(StringUtils.EMPTY);
            log.info("  Resolved appellant postcode: {}.", postcode);
        }

        return postcode;
    }
}
