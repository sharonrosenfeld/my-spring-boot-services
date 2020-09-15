package com.sharon.dataws.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationProperties {
    Integer apiUserDataExpiration;
    Integer siteUserDataExpiration;
}
