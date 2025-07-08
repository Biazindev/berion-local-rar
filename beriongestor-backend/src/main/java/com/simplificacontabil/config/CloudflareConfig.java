package com.simplificacontabil.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cloudflare")
@Data
public class CloudflareConfig {
    private String apiToken;
    private String zoneId;
}
