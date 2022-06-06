package uk.gov.hmcts.reform.sscs.migration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;
import uk.gov.hmcts.reform.sscs.service.RegionalProcessingCenterService;
import uk.gov.hmcts.reform.sscs.service.VenueService;

@RunWith(MockitoJUnitRunner.class)
class RpcVenueServiceTest {

    @Mock
    private RegionalProcessingCenterService regionalProcessingCenterService;

    @Mock
    private VenueService venueService;

    @InjectMocks
    private RpcVenueService rpcVenueService;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    void shouldGetEpimsId_givenValidAppellant() {
        when(regionalProcessingCenterService.getByPostcode("postcode")).thenReturn(
            RegionalProcessingCenter.builder().postcode("rpcPostcode").build());
        when(venueService.getEpimsIdForActiveVenueByPostcode("rpcPostcode")).thenReturn(Optional.of("rpcEpimsId"));

        String rpcEpimsId = rpcVenueService.retrieveRpcEpimsIdForPostcode("postcode");

        assertThat(rpcEpimsId).isEqualTo("rpcEpimsId");
    }

    @Test
    void shouldGetEmptyString_givenEpimsIdIsEmpty() {
        when(regionalProcessingCenterService.getByPostcode("postcode")).thenReturn(RegionalProcessingCenter.builder().postcode("rpcPostcode").build());
        when(venueService.getEpimsIdForActiveVenueByPostcode("rpcPostcode")).thenReturn(Optional.empty());

        String rpcEpimsId = rpcVenueService.retrieveRpcEpimsIdForPostcode("postcode");

        assertThat(rpcEpimsId).isEmpty();
    }

}