package com.sharon.dataaggregator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationProperties {
    private String apiurl;
    private String siteurl;
    private Integer site_httppool_maxtotal;
    private Integer api_httppool_maxtotal;
}
