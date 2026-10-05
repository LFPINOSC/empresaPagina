package com.api.empresa.Seguridad;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityExceptionHandler
        implements AuthenticationEntryPoint, AccessDeniedHandler {


    // =====================================================
    // 401 - NO AUTENTICADO
    // =====================================================

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.core.AuthenticationException authException)
            throws IOException {

        response.setStatus(
            HttpServletResponse.SC_UNAUTHORIZED
        );

        response.setContentType(
            "application/json"
        );

        response.setCharacterEncoding(
            "UTF-8"
        );

        String mensaje = """
        {
            "status": 401,
            "error": "No autenticado",
            "mensaje": "Debe proporcionar un token JWT válido para acceder a este recurso"
        }
        """;

        response.getWriter().write(mensaje);
    }


    // =====================================================
    // 403 - SIN PERMISOS
    // =====================================================

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {

        response.setStatus(
            HttpServletResponse.SC_FORBIDDEN
        );

        response.setContentType(
            "application/json"
        );

        response.setCharacterEncoding(
            "UTF-8"
        );

        String mensaje = """
        {
            "status": 403,
            "error": "Acceso denegado",
            "mensaje": "No tiene permisos para realizar esta operación"
        }
        """;

        response.getWriter().write(mensaje);
    }
}

