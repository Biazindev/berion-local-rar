package com.simplificacontabil.tomcat;

import org.apache.catalina.core.StandardContext;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simplificacontabil.tomcat.EarlyBlockValve;

import org.apache.catalina.Context;

@Configuration
public class TomcatValveConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customValve() {
        return factory -> factory.addContextValves(new EarlyBlockValve());
    }
    @Bean
    public TomcatContextCustomizer valveRegistrar() {
        return context -> {
            if (context instanceof StandardContext standardContext) {
                standardContext.getPipeline().addValve(new EarlyBlockValve());
            }
        };
    }
}
