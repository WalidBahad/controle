package com.carrental.analyticsservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for analytics-service.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI analyticsServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Analytics Service API")
                        .description("REST API for car occupancy analytics in the Car Rental Platform")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Car Rental Platform")
                                .email("support@carrental.com")));
    }
}

