package com.sample.springtraining.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sample.springtraining.handler.CustomAccessDeniedHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final CustomAccessDeniedHandler customAccessDeniedHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 적용
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
                                                .requestMatchers("/webjars/**", "/images/**", "/css/**", "/js/**").permitAll()
                                                .requestMatchers("/api/auth/**").permitAll()
                                                .requestMatchers("/api/posts/**").permitAll() // 개발 중: Post API 전체 공개
                                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/api/users/**").hasAnyRole("ADMIN", "USER")
                                                .requestMatchers("/api/**").authenticated()
                                                .anyRequest().authenticated())
                                .exceptionHandling(exception -> exception
                                                .accessDeniedHandler(customAccessDeniedHandler))
                                .formLogin(form -> form
                                                .defaultSuccessUrl("/swagger-ui/index.html", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/")
                                                .permitAll())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/h2-console/**", "/api/posts/**")) // CSRF 비활성화
                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.sameOrigin()));

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "http://localhost:3001"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("*"));
            configuration.setAllowCredentials(true);
            
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/api/**", configuration);
            return source;
        }
}
