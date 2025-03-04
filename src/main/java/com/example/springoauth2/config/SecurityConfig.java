package com.example.springoauth2.config;

import com.example.springoauth2.service.Oauth2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Oauth2Service oauth2Service;

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    public SecurityConfig(Oauth2Service oauth2Service) {
        this.oauth2Service = oauth2Service;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/login", "/", "/oauth2/**", "/login/oauth2/code/github").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/logout").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/profile").authenticated()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oauth2Service))
                        .defaultSuccessUrl("/profile", true)
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/login/oauth2/code/*")))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if (authentication != null) {
                                logger.info("Успешный выход {} из системы", authentication.getName());
                            } else {
                                logger.warn("Выход из системы: пользователь неизвестен (аутентификация уже очищена)");
                            }
                            response.sendRedirect("/");
                        }))
                .build();
    }
}
