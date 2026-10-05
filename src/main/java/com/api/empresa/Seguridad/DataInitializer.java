package com.api.empresa.Seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.api.empresa.Entidades.Rol;
import com.api.empresa.Entidades.Usuario;
import com.api.empresa.Repositorios.UsuarioRepositorio;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Verificar si ya existe el administrador
        if (!usuarioRepositorio.existsByUsername("admin")) {

            Usuario admin = new Usuario();

            admin.setUsername("admin");

            admin.setPassword(
                passwordEncoder.encode("Admin123*")
            );

            admin.setRol(Rol.ADMIN);

            admin.setActivo(true);

            // ADMIN no necesita estar asociado
            // a un cliente
            admin.setCliente(null);

            usuarioRepositorio.save(admin);

            System.out.println(
                "======================================"
            );

            System.out.println(
                "USUARIO ADMIN CREADO"
            );

            System.out.println(
                "Usuario: admin"
            );

            System.out.println(
                "======================================"
            );

        } else {

            System.out.println(
                "El usuario ADMIN ya existe"
            );
        }
    }
}

