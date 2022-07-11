package uk.gov.hmcts.reform.sscs.migration.helper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appointee;
import uk.gov.hmcts.reform.sscs.ccd.domain.YesNo;

class PostcodeResolverTest {

    private final PostcodeResolver unitUnderTest = new PostcodeResolver();

    @Test
    void shouldReturnBlankPostcode_givenAppointeeAndPostcodeDoesNotExist() {
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
    void shouldReturnBlankPostcode_givenNoAppointeeAndPostcodeDoesNotExist() {
        Appellant testAppellant = Appellant.builder()
            .address(Address.builder().build())
            .isAppointee(YesNo.NO.getValue())
            .build();

        String actualPostcode = unitUnderTest.resolvePostcode(Appeal.builder()
            .appellant(testAppellant)
            .build());

        assertThat(actualPostcode).isEmpty();
    }


    @Test
    void shouldReturnBlankPostcode_givenAppointeeAndAddressDoNotExist() {
        Appellant testAppellant = Appellant.builder().isAppointee(YesNo.YES.getValue()).build();

        String actualPostcode = unitUnderTest.resolvePostcode(Appeal.builder()
            .appellant(testAppellant)
            .build());

        assertThat(actualPostcode).isEmpty();
    }

    @Test
    void shouldReturnBlankPostcode_givenNoAppointeeAddressDoesNotExist() {
        Appellant testAppellant = Appellant.builder().isAppointee(YesNo.NO.getValue()).build();

        String actualPostcode = unitUnderTest.resolvePostcode(Appeal.builder()
            .appellant(testAppellant)
            .build());

        assertThat(actualPostcode).isEmpty();
    }


    @ParameterizedTest
    @CsvSource({"YES,SD1 7ST", "NO,LU6 0NW"})
    void shouldResolveCorrectPostcode(YesNo appointee, String expectedPostcode) {
        String appointeePostcode = "SD1 7ST";
        String appellantPostcode = "LU6 0NW";
        Appellant testAppellant = Appellant.builder()
            .appointee(Appointee.builder()
                .address(Address
                    .builder()
                .postcode(appointeePostcode)
                    .build())
                .build())
            .address(Address
                .builder()
                .postcode(appellantPostcode)
                .build())
            .isAppointee(appointee.getValue()).build();

        String actualPostcode = unitUnderTest.resolvePostcode(Appeal.builder()
            .appellant(testAppellant)
            .build());

        assertThat(actualPostcode).isEqualTo(expectedPostcode);
    }

}
