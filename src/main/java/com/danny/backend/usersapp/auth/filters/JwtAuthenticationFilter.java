package com.danny.backend.usersapp.auth.filters;

import com.danny.backend.usersapp.models.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override //Intentar autenticacion con el usuario y contraseña, solo se ejecuta cuando se hace un POST a /login
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        User user = null;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class); // Lee el body de la peticion y lo convierte en un objeto User
            username = user.getUsername();
            password = user.getPassword();
            logger.info("Username desde request " + username);
            logger.info("Password desde request " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password); // Crea un token con el username y password
        return this.authenticationManager.authenticate(authToken);
    }

    @Override // Si la autenticacion fue exitosa
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername(); // Obtiene el username del usuario autenticado

        // Creacion del token
        String originalInput = "token_de_prueba." + username;
        String token = Base64.getEncoder().encodeToString(originalInput.getBytes()); // Genera un token de prueba
        response.addHeader("Authorization", "Bearer " + token); // Agrega el token al header de la respuesta

        // body se convertira en un objeto JSON
        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("message", String.format("Hola %s, has iniciado sesion con exito", username));
        body.put("username", username);

        response.getWriter().write(new ObjectMapper().writeValueAsString(body)); // Escribe el body de la respuesta
        response.setStatus(200);
        response.setContentType("application/json");
    }

    @Override // Si la autenticacion fue fallida
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Error de autenticacion: username o password incorrecto");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body)); // Escribe el body de la respuesta
        response.setStatus(401);
        response.setContentType("application/json");
    }
}
