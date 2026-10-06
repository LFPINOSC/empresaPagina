package com.api.empresa.Controladores;


import com.api.empresa.Entidades.Rol;
import com.api.empresa.Entidades.Usuario;
import com.api.empresa.Servicios.UsuarioServicio;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(
            @Valid @RequestBody Usuario usuario) {

        Usuario usuarioCreado =
                usuarioServicio.crearUsuario(usuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioCreado);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {

        return ResponseEntity.ok(
                usuarioServicio.listarUsuarios()
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                usuarioServicio.buscarPorId(id)
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Usuario> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activo) {

        return ResponseEntity.ok(
                usuarioServicio.cambiarEstado(id, activo)
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/rol")
    public ResponseEntity<Usuario> cambiarRol(
            @PathVariable Long id,
            @RequestParam Rol rol) {

        return ResponseEntity.ok(
                usuarioServicio.cambiarRol(id, rol)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{usuarioId}/cliente/{clienteId}")
    public ResponseEntity<Usuario> asociarCliente(
            @PathVariable Long usuarioId,
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(
                usuarioServicio.asociarCliente(
                        usuarioId,
                        clienteId
                )
        );
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @GetMapping("/perfil")
    public ResponseEntity<Usuario> obtenerPerfil(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(
                usuarioServicio.buscarPorUsername(username)
        );
   }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{usuarioId}/cliente")
    public ResponseEntity<Usuario> desvincularCliente(
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok(
                usuarioServicio.desvincularCliente(
                        usuarioId
                )
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Long id) {
        usuarioServicio.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @PutMapping("/perfil")
    public ResponseEntity<Usuario> actualizarPerfil(
                Authentication authentication,
                @RequestBody Usuario usuarioActualizado) {
        String username = authentication.getName();
        Usuario usuario =
                usuarioServicio.buscarPorUsername(username);
        Usuario actualizado =
                usuarioServicio.actualizarPerfil(
                        usuario.getId(),
                        usuarioActualizado
                );
        return ResponseEntity.ok(actualizado);
    }
}
