package uk.gov.hmcts.reform.sscs.migration.helper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appointee;
import uk.gov.hmcts.reform.sscs.ccd.domain.YesNo;

class PostcodeResolverTest {

    private final PostcodeResolver unitUnderTest = new PostcodeResolver();

    @Test
    void shouldReturnBlankPostcode_givenAppointeeAndAppellantPostcodeDoNotExist() {
        Appellant testAppellant = Appellant.builder()
            .address(Address.builder().build())
            .isAppointee(YesNo.YES.getValue())
            .appointee(Appointee.builder()
                .address(Address.builder().build())
                .build())
            .build();

        String actualPostcode = unitUnderTest.resolvePostcode(Appeal.builder()
            .appellant(testAppellant)
            .build());

        assertThat(actualPostcode).isEmpty();
    }

    @Test
    void shouldReturnBlankPostcode_givenAppointeeAndAppellantAddressDoNotExist() {
        Appellant testAppellant = Appellant.builder().isAppointee(YesNo.YES.getValue()).build();

        String actualPostcode = unitUnderTest.resolvePostcode(Appeal.builder()
            .appellant(testAppellant)
            .build());

        assertThat(actualPostcode).isEmpty();
    }

}
