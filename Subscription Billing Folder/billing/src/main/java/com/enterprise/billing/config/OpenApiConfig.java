package com.enterprise.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Subscription Billing System API")
                        .version("1.0.0")
                        .description("Enterprise REST APIs for managing users, billing, and logs.")
                        .contact(new Contact().name("Enterprise Architecture Team")));
    }
}