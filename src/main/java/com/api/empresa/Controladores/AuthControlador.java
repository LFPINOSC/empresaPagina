package com.api.empresa.Controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.api.empresa.DTO.AuthResponse;
import com.api.empresa.DTO.LoginRequest;
import com.api.empresa.DTO.RegisterRequest;
import com.api.empresa.Entidades.Usuario;
import com.api.empresa.Seguridad.JwtService;
import com.api.empresa.Servicios.UsuarioServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthControlador {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioServicio usuarioServicio;

    public AuthControlador(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UsuarioServicio usuarioServicio) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioServicio = usuarioServicio;
    }

    // =========================================================
    // REGISTRAR USUARIO + CLIENTE
    // PÚBLICO
    // =========================================================

    @PostMapping("/register")
    public ResponseEntity<?> registrar(
            @Valid @RequestBody RegisterRequest request) {

        Usuario usuario =
                usuarioServicio.registrarUsuarioConCliente(
                        request.getUsername(),
                        request.getPassword(),
                        request.getCliente()
                );

        return ResponseEntity.ok(
                "Usuario y cliente registrados correctamente"
        );
    }

    // =========================================================
    // LOGIN
    // PÚBLICO
    // =========================================================

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token =
                jwtService.generarToken(userDetails);

        Usuario usuario =
                usuarioServicio.buscarPorUsername(
                        request.getUsername()
                );

        return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        usuario.getUsername(),
                        usuario.getRol().name()
                )
        );
    }
}

