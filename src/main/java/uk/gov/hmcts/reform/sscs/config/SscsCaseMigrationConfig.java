package uk.gov.hmcts.reform.sscs.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableFeignClients(basePackages = {
    "uk.gov.hmcts.reform.sscs.client"
})
@ComponentScan(basePackages = "uk.gov.hmcts.reform.sscs", lazyInit = true)
@PropertySource("classpath:application.properties")
public class SscsCaseMigrationConfig {
}
