package com.simplificacontabil.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapeia tudo que vem para '/' e subcaminhos para a pasta 'static' no classpath
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        // Você também pode adicionar /js/**, /css/**, etc. explicitamente se quiser
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/static/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/static/css/");
        registry.addResourceHandler("/media/**").addResourceLocations("classpath:/static/static/media/");

        // Para os arquivos na raiz (favicon, manifest, etc.)
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/manifest.json").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/robots.txt").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/service-worker.js").addResourceLocations("classpath:/static/");


        // IMPORTANTE: Chamar o super para manter o comportamento padrão do Spring Boot para outros recursos
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
