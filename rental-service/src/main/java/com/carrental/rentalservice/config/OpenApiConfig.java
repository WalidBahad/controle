package com.carrental.rentalservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for rental-service.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI rentalServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Rental Service API")
                        .description("REST API for managing car rentals in the Car Rental Platform")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Car Rental Platform")
                                .email("support@carrental.com")));
    }
}

