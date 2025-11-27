package com.jian.community.global.config;

import com.jian.community.global.exception.CustomAccessDeniedHandler;
import com.jian.community.global.exception.CustomAuthenticationEntryPoint;
import com.jian.community.global.filter.JwtAuthenticationFilter;
import com.jian.community.global.provider.JwtAuthenticationProvider;
import com.jian.community.global.util.ExcludeUrlPatternMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CorsFilter corsFilter,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationProvider jwtAuthenticationProvider,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        // 기본 설정
        http.csrf(AbstractHttpConfigurer::disable)
            .securityContext(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(config ->
                    config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 인가 설정
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(request ->
                    ExcludeUrlPatternMatcher.matchesAny(request.getMethod(), request.getRequestURI())).permitAll()
            .anyRequest().hasAnyAuthority("ADMIN", "USER"));

        // 인증 설정
        http.authenticationProvider(jwtAuthenticationProvider);

        // 필터 설정
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(corsFilter, JwtAuthenticationFilter.class);

        // 예외 처리 설정
        http.exceptionHandling(config ->
            config.authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

