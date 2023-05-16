package com.danny.backend.usersapp.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.*;

import static com.danny.backend.usersapp.auth.TokenJwtConfig.*;

// Válida el token que se envia en el header de la peticion en cada peticion que se haga
public class JwtValidationFilter extends BasicAuthenticationFilter {
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_AUTHORIZATION); // Obtiene el header de la peticion

        if (header == null || !header.startsWith(PREFIX_TOKEN)) { // Si el header es nulo o no empieza con Bearer
            chain.doFilter(request, response); // Continua con la cadena de filtros
            return;
        }

        String token = header.replace(PREFIX_TOKEN, ""); // Obtiene el token
        byte[] tokenDecodedBytes = Base64.getDecoder().decode(token); // Decodifica el token
        String tokenDecoded = new String(tokenDecodedBytes); // Convierte el token decodificado a String

        String[] parts = tokenDecoded.split("\\."); // Separa el token
        System.out.println(Arrays.toString(parts));

        String secret = parts[0]; // Obtiene el secret
        String username = parts[1]; // Obtiene el username

        if (SECRET_KEY.equals(secret)) { // Si el secret es igual a token_de_prueba
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities); // Crea un token con el username y password

            SecurityContextHolder.getContext().setAuthentication(authToken); // Agrega el token al contexto de seguridad


            chain.doFilter(request, response); // Continua con la cadena de filtros

        } else {
            Map<String, String> body = new HashMap<>();
            body.put("message", "El token JWT no es válido ");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Si el secret no es igual a token_de_prueba, retorna un error 401
            response.getWriter().write(new ObjectMapper().writeValueAsString(body)); // Escribe el body de la respuesta
            response.setContentType("application/json"); // Establece el tipo de contenido de la respuesta

        }

    }
}
