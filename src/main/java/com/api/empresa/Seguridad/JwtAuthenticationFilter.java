package com.api.empresa.Seguridad;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");


        if (authHeader == null ||
            !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(
                request,
                response
            );

            return;
        }

        String token =
                authHeader.substring(7);


        try {

            String username =
                    jwtService.obtenerUsername(token);

            if (username != null &&
                SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {


                UserDetails userDetails =
                        userDetailsService
                            .loadUserByUsername(username);

                if (jwtService.validarToken(
                        token,
                        userDetails)) {

                    System.out.println(
                        "Token JWT válido para el usuario: " +
                        username
                    );
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );


                    authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                            .buildDetails(request)
                    );


                    SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                            authentication
                        );
                    System.out.println(
                        "Autenticación establecida para el usuario: "
                        + username
                        + SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                    );
                }
            }

        } catch (Exception e) {

            System.err.println("Error al procesar el token JWT: " + e.getMessage());
            e.printStackTrace();
        }


        filterChain.doFilter(
            request,
            response
        );
    }
}

