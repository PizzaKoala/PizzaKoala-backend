package com.PizzaKoala.Pizza.global.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

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
                        .description("PizzaKoala OpenAPI documentation. \n\n### 🔥 [JWT 인증 방법 영상 보기](https://pizzakoala.s3.ap-northeast-2.amazonaws.com/2025-01-293.51.11-ezgif.com-speed.gif) \uD83D\uDC48 **클릭해주세요!** 🔥\n\n \uD83D\uDE80 **로그인 API에서 JWT 토큰을 발급받아서 수동으로 인증해주세요!**")
//                        .description("PizzaKoala OpenAPI documentation. \n\n[JWT 인증 방법 영상 보기](https://pizzakoala.s3.ap-northeast-2.amazonaws.com/2025-01-293.51.11-ezgif.com-speed.gif) 로그인api에서 jwt토큰을 발급받아서 수동으로 인증해주세요.") // 마크다운 형식으로 영상 링크 추가
                        .version("1.0"));

    }
    @Bean
    public OpenApiCustomizer customTagsOrder() {
        return openApi -> {
            openApi.setTags(Arrays.asList(
                    new Tag().name("공개APIs").description("로그인 필요없는 APIs: 회원가입, 로그인 API- 로그인/가입 후 response header에서 JWT 토큰을(Bearer) 복사해서 인증(Authorize) 해주세요. 이 글 위에 오른쪽에 [Authorize] 버튼누르고 붙여넣기하면 됩니다. \n\n \uD83D\uDE80 **로그인 API에서 JWT 토큰을 발급받아서 수동으로 인증해주세요!**"),
                    new Tag().name("메인페이지APIs").description("게시글 좋아요순, 팔로우순 다건 조회"),
                    new Tag().name("게시글쓰기APIs").description("게시글 쓰기, 수정, 삭제 기능"),
                    new Tag().name("게시글조회APIs").description("게시글 단건조회, 다건조회(맴버 아이디로 특정맴버 조회, 내 게시글 조회)기능"),
                    new Tag().name("댓글쓰기APIs").description("게시글 댓글 쓰기, 수정, 삭제 기능"),
                    new Tag().name("좋아요APIs").description("특정 게시글 좋아요하기, 좋아요 취소하기 기능"),
                    new Tag().name("팔로우APIs").description("팔로우 기능+ 팔로우,팔로워 다건 조회 기능"),
                    new Tag().name("검색APIs").description("키워드 검색기능(게시글:최신순,추천순),(맴버:팔로워순,최근게시글순)"),
                    new Tag().name("캘린더APIs").description("📅 **캘린더 기능 미리보기!** 게시글이 올라간 날짜를 한눈에 확인할 수 있어요. 👉 [**캘린더 UI 보기**](https://pizzakoala.s3.ap-northeast-2.amazonaws.com/calender.png) 클릭하면 연단위 캘린더를 확인할 수 있습니다!"),
                    new Tag().name("알림APIs")
                            .description("🔥 실시간 SSE 알림 시스템! 🔥 📢 댓글, 좋아요, 팔로우 이벤트가 발생하면 실시간 알림이 전송됩니다.⚡  **Swagger에서는 SSE 실시간 알림이 보이지 않으므로, 포스트맨을 활용한 실제 동작 영상을 확인하세요!**🎬 \n **[🔗 실시간 알림 테스트 영상 보기](https://youtu.be/59KK-ptg6wg)**"),
                    new Tag().name("알림APIs").description("실시간 SSE 알림, 내 알림 리스트"),
                    new Tag().name("토큰재발급APIs").description("토큰 재발급 요청을 합니다.")
            ));
        };
    }
}
