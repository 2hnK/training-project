package com.sample.springtraining.config;

import javax.sql.DataSource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sample.springtraining.handlers.CustomAccessDeniedHandler;
import com.sample.springtraining.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private CustomAccessDeniedHandler customAccessDeniedHandler;
        private DataSource dataSource;

        @Autowired
        public SecurityConfig(CustomAccessDeniedHandler customAccessDeniedHandler, DataSource dataSource) {
                this.customAccessDeniedHandler = customAccessDeniedHandler;
                this.dataSource = dataSource;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
                                .requestMatchers("/webjars/**", "/images/**", "/css/**", "/js/**").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
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
                                                .ignoringRequestMatchers("/h2-console/**"))
                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.sameOrigin()));

                return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
                return new CustomUserDetailsService();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
}
