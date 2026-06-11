package com.enterprise.drs.config;

// Ensure these exact three imports are present and correct
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Enterprise Disaster Recovery & Backup System API")
                        .version("1.0.0")
                        .description("Automated schema backups, physical object tracking, and restore operations pipeline."));
    }
}