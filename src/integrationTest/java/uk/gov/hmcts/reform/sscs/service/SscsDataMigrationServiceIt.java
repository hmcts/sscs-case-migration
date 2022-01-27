package uk.gov.hmcts.reform.sscs.service;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;
import uk.gov.hmcts.reform.migration.CaseMigrationRunner;
import uk.gov.hmcts.reform.migration.ccd.CoreCaseDataService;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService;
import uk.gov.hmcts.reform.sscs.config.SscsCaseMigrationConfig;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CaseMigrationRunner.class, SscsCaseMigrationConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SscsDataMigrationServiceIt {

    private static CaseDetails caseDetails;
    private static Map<String, Object> caseData = new HashMap<>();
    protected static SscsCaseData sscsCaseData;

    @TestConfiguration
    public static class TestConfig {
        @Bean
        @Primary
        public CoreCaseDataService mockCoreCaseDataService() {
            caseDetails = CaseDetails.builder().jurisdiction("SSCS").data(caseData).id(1L).build();
            CoreCaseDataService ccds = mock(CoreCaseDataService.class);
            when(ccds.fetchOne(anyString(), anyString())).thenReturn(caseDetails);
            return ccds;
        }

        @Bean
        @Primary
        public SscsCcdConvertService mockSscsCcdConvertService() {
            sscsCaseData = SscsCaseData.builder()
                .appeal(Appeal.builder()
                    .benefitType(BenefitType.builder()
                     .code("PIP")
                     .build())
                    .appellant(Appellant.builder()
                       .name(Name.builder()
                         .firstName("First")
                         .lastName("Last")
                         .build())
                       .build())
                    .build())
                .build();
            SscsCcdConvertService sccs = mock(SscsCcdConvertService.class);
            when(sccs.getCaseData(caseData)).thenReturn(sscsCaseData);
            return sccs;
        }

        @Bean
        @Primary
        public IdamClient mockIdamClient() {
            IdamClient idc = mock(IdamClient.class);
            when(idc.authenticateUser(anyString(), anyString())).thenReturn("token");
            when(idc.getUserDetails("token")).thenReturn(UserDetails.builder().id("id").build());
            return idc;
        }
    }

    @Autowired
    private IdamClient idamClient;

    @Autowired
    private CoreCaseDataService coreCaseDataService;

    @Autowired
    private SscsCcdConvertService sscsCcdConvertService;

    @Autowired
    private SscsDataMigrationService sscsDataMigrationService;


    @Test
    public void verifyServiceCall() {
        Assertions.assertNotNull(sscsCaseData);
    }
}
