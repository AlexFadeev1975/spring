package org.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    UserDetailsServiceImpl userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
@Bean
    public ReactiveAuthenticationManager authenticationManager (ReactiveUserDetailsService userDetailsService,
                                                                PasswordEncoder passwordEncoder) {

        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

@Bean
    public SecurityWebFilterChain filterChain (ServerHttpSecurity http, ReactiveAuthenticationManager authenticationManager) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authenticationManager(authenticationManager)
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec
                .pathMatchers(HttpMethod.POST,"/users/api/v1/create").permitAll()
                .pathMatchers(HttpMethod.GET,"/users/api/v1/users").hasAnyRole("USER", "MANAGER")
                .pathMatchers(HttpMethod.GET,"/users/api/v1/{id}").hasAnyRole("USER", "MANAGER")
                .pathMatchers(HttpMethod.PUT,"/users/api/v1/update").hasAnyRole("USER", "MANAGER")
                .pathMatchers(HttpMethod.DELETE,"/users/api/v1/{id}").hasAnyRole("USER", "MANAGER")
                .pathMatchers(HttpMethod.GET, "tasks/api/v1/tasks").hasAnyRole("USER", "MANAGER")
                .pathMatchers(HttpMethod.GET,"tasks/api/v1/{id}").hasAnyRole("USER", "MANAGER")
                .pathMatchers(HttpMethod.PUT,"tasks/api/v1/observer").hasAnyRole("USER", "MANAGER")
                .pathMatchers(HttpMethod.POST,"tasks/api/v1/create").hasRole ("MANAGER")
                .pathMatchers(HttpMethod.PUT,"tasks/api/v1/update").hasRole ("MANAGER")
                .pathMatchers(HttpMethod.DELETE,"tasks/api/v1/{id}").hasRole ("MANAGER"))
                .httpBasic()
                .and()
                .build();
    }

}
