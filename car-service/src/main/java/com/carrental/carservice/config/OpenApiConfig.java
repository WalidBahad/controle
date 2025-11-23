package com.carrental.carservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for car-service.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Car Service API")
                        .description("REST API for managing car entities in the Car Rental Platform")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Car Rental Platform")
                                .email("walidbahad@gmail.com")));
    }
}

