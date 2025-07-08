package com.simplificacontabil.integracoes.nuvemfiscal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NuvemFiscalConfig {

    @Bean(name = "nuvemRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
