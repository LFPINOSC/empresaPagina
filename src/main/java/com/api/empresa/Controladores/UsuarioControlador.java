package com.api.empresa.Controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.api.empresa.Entidades.Rol;
import com.api.empresa.Entidades.Usuario;
import com.api.empresa.Servicios.UsuarioServicio;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;


    // =========================================================
    // LISTAR USUARIOS
    // ADMIN
    // =========================================================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {

        return ResponseEntity.ok(
            usuarioServicio.listarUsuarios()
        );
    }


    // =========================================================
    // BUSCAR USUARIO POR ID
    // ADMIN
    // =========================================================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            usuarioServicio.buscarPorId(id)
        );
    }


    // =========================================================
    // CAMBIAR ESTADO
    // ADMIN
    // =========================================================

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Usuario> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activo) {

        return ResponseEntity.ok(
            usuarioServicio.cambiarEstado(
                id,
                activo
            )
        );
    }


    // =========================================================
    // CAMBIAR ROL
    // ADMIN
    // =========================================================

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/rol")
    public ResponseEntity<Usuario> cambiarRol(
            @PathVariable Long id,
            @RequestParam Rol rol) {

        return ResponseEntity.ok(
            usuarioServicio.cambiarRol(
                id,
                rol
            )
        );
    }


    // =========================================================
    // ASOCIAR CLIENTE
    // ADMIN
    // =========================================================

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


    // =========================================================
    // DESVINCULAR CLIENTE
    // ADMIN
    // =========================================================

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


    // =========================================================
    // ELIMINAR USUARIO
    // ADMIN
    // =========================================================

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Long id) {

        usuarioServicio.eliminarUsuario(id);

        return ResponseEntity.noContent().build();
    }
}
