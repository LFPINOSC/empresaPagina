package com.api.empresa.Controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.empresa.Entidades.Cliente;
import com.api.empresa.Servicios.ClienteServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteControlador {

    @Autowired
    private ClienteServicio clienteServicio;

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @PostMapping
    public ResponseEntity<Cliente> guardarCliente(
            @Valid @RequestBody Cliente cliente) {

        Cliente clienteGuardado =
                clienteServicio.guardarCliente(cliente);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteGuardado);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientes() {

        return ResponseEntity.ok(
                clienteServicio.obtenerTodosLosClientes()
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @GetMapping("/id/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(
            @PathVariable Long id) {

        Cliente cliente =
                clienteServicio.obtenerClientePorId(id);

        return ResponseEntity.ok(cliente);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Cliente> obtenerClientePorCedula(
            @PathVariable String cedula) {

        Cliente cliente =
                clienteServicio.obtenerClientePorCedula(cedula);

        return ResponseEntity.ok(cliente);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @GetMapping("/direccion/{direccion}")
    public ResponseEntity<List<Cliente>> obtenerClientesPorDireccion(
            @PathVariable String direccion) {

        return ResponseEntity.ok(
                clienteServicio.obtenerClientesPorDireccion(direccion)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/id/{id}")
    public ResponseEntity<Cliente> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody Cliente clienteActualizado) {

        Cliente cliente =
                clienteServicio.actualizarCliente(
                        id,
                        clienteActualizado
                );

        return ResponseEntity.ok(cliente);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> eliminarCliente(
            @PathVariable Long id) {

        clienteServicio.eliminarCliente(id);

        return ResponseEntity.noContent().build();
    }
}

