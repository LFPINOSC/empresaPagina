package com.api.empresa.Seguridad;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;


    // =====================================================
    // OBTENER CLAVE SECRETA
    // =====================================================

    private SecretKey getKey() {

        return Keys.hmacShaKeyFor(
            secret.getBytes(StandardCharsets.UTF_8)
        );
    }


    // =====================================================
    // GENERAR TOKEN
    // =====================================================

    public String generarToken(
            UserDetails userDetails) {

        return Jwts.builder()

                .subject(
                    userDetails.getUsername()
                )

                .issuedAt(
                    new Date()
                )

                .expiration(
                    new Date(
                        System.currentTimeMillis()
                        + expiration
                    )
                )

                .signWith(
                    getKey()
                )

                .compact();
    }


    // =====================================================
    // OBTENER USERNAME DEL TOKEN
    // =====================================================

    public String obtenerUsername(
            String token) {

        return obtenerClaims(token)
                .getSubject();
    }


    // =====================================================
    // VALIDAR TOKEN
    // =====================================================

    public boolean validarToken(
            String token,
            UserDetails userDetails) {

        String username =
                obtenerUsername(token);

        return username.equals(
                    userDetails.getUsername()
                )
                && !tokenExpirado(token);
    }


    // =====================================================
    // VERIFICAR EXPIRACIÓN
    // =====================================================

    private boolean tokenExpirado(
            String token) {

        return obtenerClaims(token)
                .getExpiration()
                .before(new Date());
    }


    // =====================================================
    // OBTENER CLAIMS
    // =====================================================

    private Claims obtenerClaims(
            String token) {

        return Jwts.parser()

                .verifyWith(
                    getKey()
                )

                .build()

                .parseSignedClaims(
                    token
                )

                .getPayload();
    }
}

