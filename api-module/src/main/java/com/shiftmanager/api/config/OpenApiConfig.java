package com.shiftmanager.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for API documentation
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configure OpenAPI documentation for the API
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Shift Manager API")
                        .description("API for managing shift workers with flexible schedules")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Shift Manager Team")
                                .email("support@shiftmanager.com")
                                .url("https://shiftmanager.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
