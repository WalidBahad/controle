package com.carrental.paymentservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for payment-service.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI paymentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Payment Service API")
                        .description("REST API for processing payments in the Car Rental Platform (Mock Implementation)")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Car Rental Platform")
                                .email("support@carrental.com")));
    }
}

