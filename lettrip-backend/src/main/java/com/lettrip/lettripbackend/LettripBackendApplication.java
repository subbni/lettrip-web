package com.lettrip.lettripbackend;

import com.lettrip.lettripbackend.configuration.AuthProperties;
import com.lettrip.lettripbackend.configuration.WebMvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableJpaAuditing
@EnableConfigurationProperties(AuthProperties.class)
@SpringBootApplication
public class LettripBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LettripBackendApplication.class, args);
	}


}
