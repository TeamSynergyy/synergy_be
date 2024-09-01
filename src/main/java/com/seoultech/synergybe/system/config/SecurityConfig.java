package com.seoultech.synergybe.system.config;

import com.seoultech.synergybe.domain.auth.JwtAuthenticationProvider;
import com.seoultech.synergybe.domain.common.CustomPasswordEncoder;
import com.seoultech.synergybe.domain.user.repository.UserRefreshTokenFactory;
import com.seoultech.synergybe.domain.user.service.UserRefreshTokenReader;
import com.seoultech.synergybe.system.security.*;
import com.seoultech.synergybe.system.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserRefreshTokenReader userRefreshTokenReader;
    private final UserRefreshTokenFactory userRefreshTokenFactory;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtAuthenticationProvider authenticationProvider;
    private final CookieUtil cookieUtil;
    @Bean
    public CustomPasswordEncoder customPasswordEncoder() {
        return new BCryptCustomPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Authenticatoin, 토큰에 대해 인증
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userRefreshTokenReader, userRefreshTokenFactory, cookieUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));

        return filter;
    }


    // Authorization, 식별된 사용자에 대해 권한 부여, 인가
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService, authenticationProvider, userRefreshTokenReader,userRefreshTokenFactory, cookieUtil);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            CorsFilter corsFilter

    ) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable)
                    .logout(AbstractHttpConfigurer::disable)
                    .rememberMe(AbstractHttpConfigurer::disable)
                    .anonymous(AbstractHttpConfigurer::disable)
                    .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilter(corsFilter)
                    .addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class)
                    .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .headers(AbstractHttpConfigurer::disable)
                    .cors(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/api/v1/users/refresh-token", "/api/v1/users/**", "/api/v1/users/login").permitAll() // 특정 경로 허용
                            .anyRequest().authenticated() // 나머지 요청은 인증 필요
                    );

        return httpSecurity.build();
    }


    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(
                List.of("http://localhost:5173", "http://localhost:5174", "http://localhost:4173"));
        config.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}