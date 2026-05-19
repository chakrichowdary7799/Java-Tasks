package com.enterprise.warehouse.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Warehouse Allocation System API")
                        .version("1.0.0")
                        .description("Enterprise RESTful API engine for automated inventory allocation, cross-docking distribution, and real-time multi-warehouse stock balancing.")
                        .contact(new Contact()
                                .name("Enterprise Engineering Team")
                                .email("support@enterprise.com")));
    }
}
