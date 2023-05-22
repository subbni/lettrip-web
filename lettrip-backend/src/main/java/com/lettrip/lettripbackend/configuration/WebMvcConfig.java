package com.lettrip.lettripbackend.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET","HEAD","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}

