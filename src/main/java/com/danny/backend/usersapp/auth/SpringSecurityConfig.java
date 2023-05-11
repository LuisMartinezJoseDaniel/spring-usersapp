package com.danny.backend.usersapp.auth;

import com.danny.backend.usersapp.auth.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/users") // Permite solo el metodo GET sobre esa ruta
                .permitAll() // Permite a todos los usuarios acceder a la ruta
                .anyRequest().authenticated() // Cualquier otra ruta requiere autenticacion
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager())) // Agrega el filtro de autenticacion
                .csrf(AbstractHttpConfigurer::disable) // Utilizando un cliente como React es necesario deshabilitarlo en otro caso como thimeleaf no es necesario
                .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();

    }
}
