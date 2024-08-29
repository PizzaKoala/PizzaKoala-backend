package com.PizzaKoala.Pizza.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
//    @Bean
//    public OpenAPI OpenAPIConfig() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("PizzaKoala")
//                        .description("피자코알라 인터페이스 규격서")
//                        .version("1.0.0"));
//    }
    @Bean
    public OpenAPI OpenAPIConfig() {
        return new OpenAPI()
                .info(new Info().title("PIZZA KOALA API")
                        .version("1.0")
                        .description("PizzaKoala OPEN API documentation"));
    }







}
