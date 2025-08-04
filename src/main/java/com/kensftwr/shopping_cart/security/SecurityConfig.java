package com.kensftwr.shopping_cart.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final List<String> SECURED_URLS =
            List.of("/api/v1/carts/**", "/api/v1/cartItems/**");

    private final MyUserDetailsService userDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtUtil jwtUtil;

    // Password encoder bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // JWT Auth Token Filter bean
    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter( jwtUtil,userDetailsService); // Pass required dependencies
    }


    // AuthenticationManager bean (auto-configures with userDetailsService + passwordEncoder)
    //expose an AuthenticationManager bean in your Spring context so that you can manually
    // inject and use it — for example, in your login controller or service.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        What does config.getAuthenticationManager() do?
//        AuthenticationConfiguration is a Spring Security-provided class.
//        It internally builds the AuthenticationManager using:
//        Your UserDetailsService
//        Your PasswordEncoder
//        Any configured AuthenticationProviders (like DaoAuthenticationProvider)
//        Calling .getAuthenticationManager() returns that fully set up AuthenticationManager.
        return config.getAuthenticationManager();
    }


    // Security filter chain config
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->auth.requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated() // allow public auth routes
                        .anyRequest().permitAll() // protect everything else
                )
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

