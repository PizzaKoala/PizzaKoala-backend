package com.PizzaKoala.Pizza.global.config;

import com.PizzaKoala.Pizza.global.repository.RefreshRepository;
import com.PizzaKoala.Pizza.global.util.JWTTokenUtils;
import com.PizzaKoala.Pizza.global.exception.CustomAuthenticationEntryPoint;
import com.PizzaKoala.Pizza.member.oauth2.CustomClientRegistrationRepo;
import com.PizzaKoala.Pizza.member.oauth2.CustomOAuth2UserService;
import com.PizzaKoala.Pizza.member.oauth2.handler.CustomOAuth2SuccessHandler;
import com.PizzaKoala.Pizza.global.service.AuthenticationService;
import com.PizzaKoala.Pizza.global.config.filter.CustomLogoutFilter;
import com.PizzaKoala.Pizza.global.config.filter.JWTTokenFilter;
import com.PizzaKoala.Pizza.global.config.filter.NewLoginFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;


//extends WebSecurityConfiguration
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration; //authenticationManager에서 필요한 인자라서 존재한다. :(
    private final JWTTokenUtils jwtUtil;
    private final RefreshRepository refreshRepository;
    private final CustomClientRegistrationRepo customClientRegistrationRepo;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final AuthenticationService authenticationService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable) //이거 disable하면 시큐리티 필터의 usernamePasswordAUthenticationFilter도 함꼐 disable된다. 그래서 이필터를 강제로 커스텀해서 등록해야한다.
                .httpBasic(AbstractHttpConfigurer::disable);
        http
                .csrf(AbstractHttpConfigurer::disable);// jwt를 발급해서 stateless방식으로 세션을 관리해서 안해도 된다.
        http
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/*/login","/oauth2/**","login/oauth2/**", "/api/*/join",
//                                "/api/*/reissue","/h2-console/**","/api/*/posts/liked",
//                                "/api/*/search/posts/{keyword}/likes","/swagger-ui/**", "/v3/api-docs/**",
//                                "/login","/meep","/test","/favicon.ico").permitAll()
//                        .requestMatchers("/api/*/admin").hasRole("ADMIN")
                        .requestMatchers("/api/*/login","/oauth2/**","login/oauth2/**", "/api/*/join","/api/*/reissue","/h2-console/**","/api/*/posts/liked","/api/*/search/posts/{keyword}/likes").permitAll()
                        .requestMatchers("/api/*/admin").hasRole("ADMIN")
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**","/login","/meep","/favicon.ico").permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated());

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));//jwt는 항상 세션을 stateless상태로 해야한다.
        http
                .oauth2Login(oauth2 -> oauth2
                        .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository())
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(customOAuth2SuccessHandler)
                );
//        http
//                .oauth2Login((oauth2) -> oauth2
//                        .loginPage("/login/oauth2/*")
//                        .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository())
//                        .userInfoEndpoint((userInfoEndpointConfig ->
//                                userInfoEndpointConfig.userService(customOAuth2UserService)))
//                        .successHandler(customOAuth2SuccessHandler)
//                );
        http
                .addFilterBefore(new JWTTokenFilter(jwtUtil), NewLoginFilter.class);
        http
                .addFilterAfter(new JWTTokenFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource( request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // 프론트엔드 도메인
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 허용할 메서드
                    configuration.setAllowCredentials(true); // 자격 증명 허용
                    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "X-Requested-With")); // 요청 허용 헤더 구체화
                    configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie")); // 응답 노출 헤더
                    configuration.setMaxAge(3600L); // CORS 요청 캐시 지속 시간 (1시간)
                    return configuration;
                }));
        http
                .addFilterAfter(new JWTTokenFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);
        http
                .addFilterAt(new NewLoginFilter(authenticationManager(authenticationConfiguration), authenticationService), UsernamePasswordAuthenticationFilter.class);
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);
        http
                .exceptionHandling(authenticationManager -> authenticationManager.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    //이거 모든 api에 Error occurs while getting header. header is null or invalid 이거 안뜨게 해준다고 했는데 원래도 안뜸.
    // 로그인&회원가입 할떄만 뜬다.



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}