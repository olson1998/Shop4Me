package com.shop4me.productdatastream.application.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.domain.port.persisting.componentuser.ComponentUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${JWT.signature}")
    private String jwtSignature;

    private final ComponentUserRepository componentUserRepository;

    private final ObjectMapper mapper;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new Shop4MeProductDataStreamAuthService(componentUserRepository);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var authorizationFilter = new Shop4MeAuthorizationFilter(jwtSignature, mapper);

        var authenticationFilter = new Shop4MeAuthenticationFilter(
                authenticationProvider(),
                jwtSignature
        );

        authenticationFilter.setFilterProcessesUrl("/lg");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests().antMatchers("/").permitAll();
        http.authorizeHttpRequests().antMatchers("/rq")
                .hasAnyAuthority("Shop4Me_Admin", "Shop4Me_Core");

        http.addFilter(authenticationFilter);
        http.addFilterBefore(
                authorizationFilter,
                UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }

}
