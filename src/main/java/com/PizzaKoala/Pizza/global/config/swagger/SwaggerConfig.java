package com.PizzaKoala.Pizza.global.config.swagger;

import com.PizzaKoala.Pizza.domain.entity.Member;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.*;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SwaggerConfig {
    // JWT 인증 스키마 생성
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP) // HTTP 기반 인증
                .bearerFormat("JWT") // Bearer 포맷 사용
                .scheme("bearer"); // Bearer 스키마
    }

    // OAuth2 인증 스키마 생성
//    private SecurityScheme createOAuth2Scheme() {
//        return new SecurityScheme()
//                .type(SecurityScheme.Type.OAUTH2) // OAuth2 기반 인증
//                .flows(new OAuthFlows()
//                        .authorizationCode(new OAuthFlow()
//                                .authorizationUrl("https://accounts.google.com/o/oauth2/auth")// Google Authorization URL
//                                .tokenUrl("https://oauth2.googleapis.com/token") // Google Token URL
//                                .scopes(new io.swagger.v3.oas.models.security.Scopes()
//                                        .addString("email", "Access to your email")
//                                        .addString("profile", "Access to your profile"))));
//    }

    // OpenAPI 설정
    @Bean
    public OpenAPI OpenAPIConfig() {
        return new OpenAPI()
                // 인증 요구 사항 추가
                .addSecurityItem(new SecurityRequirement().addList("JWT")) // JWT 인증 추가
//                .addSecurityItem(new SecurityRequirement().addList("googleOAuth2")) // Google OAuth2 인증 추가
                .components(new Components()
                        .addSecuritySchemes("JWT", createAPIKeyScheme()))// JWT 인증 스키마
//                        .addSecuritySchemes("googleOAuth2", createOAuth2Scheme())) // OAuth2 인증 스키마
                .info(new Info()
                        .title("PIZZA KOALA API")
                        .description("PizzaKoala OpenAPI documentation")
                        .version("1.0"));
    }





}
