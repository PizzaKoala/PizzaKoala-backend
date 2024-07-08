package com.PizzaKoala.Pizza.global.config;

import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.Repository.RefreshRepository;
import com.PizzaKoala.Pizza.domain.Util.JWTTokenUtils;
import com.PizzaKoala.Pizza.domain.exception.CustomAuthenticationEntryPoint;
import com.PizzaKoala.Pizza.domain.service.MemberService;
import com.PizzaKoala.Pizza.global.config.filter.CustomLogoutFilter;
import com.PizzaKoala.Pizza.global.config.filter.JWTTokenFilter;
import com.PizzaKoala.Pizza.global.config.filter.NewLoginFilter;

import lombok.AllArgsConstructor;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;


//extends WebSecurityConfiguration
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration; //authenticationManager에서 필요한 인자라서 존재한다. :(
    private final JWTTokenUtils jwtUtil;
    private final RefreshRepository refreshRepository;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTTokenUtils jwtUtil, RefreshRepository refreshRepository) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

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
                .csrf(AbstractHttpConfigurer::disable);// jwt는 세션방식이 아니라서 csrf 공격을 방어하지 않아도 된다.
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/*/login", "/api/*/join","/api/*/reissue").permitAll()
                        .requestMatchers("/api/*/admin").hasRole("ADMIN")
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated());

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));//jwt는 항상 세션을 stateless상태로 해야한다.
        http
                .addFilterBefore(new JWTTokenFilter(jwtUtil), NewLoginFilter.class);
        http
                .addFilterAt(new NewLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);
        http
                .exceptionHandling(authenticationManager -> authenticationManager.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }
//                    .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class) //UsernamePasswordAuthenticationFilter 를 대신하는 필터라서 addfilterat을 사용한다

    //                .sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


    //이거 모든 api에 Error occurs while getting header. header is null or invalid 이거 안뜨게 해준다고 했는데 원래도 안뜸.
    // 로그인&회원가입 할떄만 뜬다.
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web -> web.ignoring().requestMatchers("^(?/api/).*"));
//
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}