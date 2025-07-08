package com.simplificacontabil.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // CORS existente
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "https://simplific-contabil.vercel.app",
                        "http://localhost:8080",
                        "https://berion.vercel.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders(
                        "Authorization",
                        "Content-Type",
                        "Accept",
                        "X-Requested-With",
                        "Cache-Control",
                        "SIMPLIFICA-API-KEY"
                )
                .exposedHeaders(
                        "Authorization",
                        "SIMPLIFICA-API-KEY",
                        "Content-Disposition"
                )
                .allowCredentials(true)
                .maxAge(3600);
    }

    // Fallback para React SPA
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{spring:\\w+}")
                .setViewName("forward:/index.html");
        registry.addViewController("/**/{spring:\\w+}")
                .setViewName("forward:/index.html");
        registry.addViewController("/{spring:\\w+}/**{spring:?!(\\.js|\\.css|\\.json|\\.png)$}")
                .setViewName("forward:/index.html");
    }
}
