package com.api.empresa.Servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.empresa.Entidades.Cliente;
import com.api.empresa.Entidades.Rol;
import com.api.empresa.Entidades.Usuario;
import com.api.empresa.Repositorios.ClienteRepositoria;
import com.api.empresa.Repositorios.UsuarioRepositorio;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ClienteRepositoria clienteRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario crearUsuario(Usuario usuario) {

        usuario.setPassword(
            passwordEncoder.encode(
                usuario.getPassword()
            )
        );

        return usuarioRepositorio.save(usuario);
    }
    
    public List<Usuario> listarUsuarios() {

        return usuarioRepositorio.findAll();
    }


    public Usuario buscarPorId(Long id) {

        return usuarioRepositorio.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Usuario no encontrado con ID: " + id
                    )
                );
    }


    public Usuario buscarPorUsername(String username) {

        return usuarioRepositorio
                .findByUsername(username)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Usuario no encontrado: " + username
                    )
                );
    }

    @Transactional
    public Usuario registrarUsuarioConCliente(
            String username,
            String password,
            Cliente cliente) {

        if (usuarioRepositorio.existsByUsername(username)) {

            throw new RuntimeException(
                "El nombre de usuario ya existe"
            );
        }

        if (cliente == null) {

            throw new RuntimeException(
                "Los datos del cliente son obligatorios"
            );
        }


        if (cliente.getCedula() == null ||
            cliente.getCedula().isBlank()) {

            throw new RuntimeException(
                "La cédula del cliente es obligatoria"
            );
        }

        if (clienteRepositorio
                .existsByCedula(cliente.getCedula())) {

            throw new RuntimeException(
                "Ya existe un cliente con la cédula: "
                + cliente.getCedula()
            );
        }


        Cliente clienteGuardado =
                clienteRepositorio.save(cliente);

        Usuario usuario = new Usuario();

        usuario.setUsername(username);

        // Nunca guardar contraseña en texto plano
        usuario.setPassword(
            passwordEncoder.encode(password)
        );

        // El registro público SIEMPRE crea USER
        usuario.setRol(Rol.USUARIO);

        usuario.setActivo(true);

        // Asociar cliente
        usuario.setCliente(clienteGuardado);


        // -----------------------------------------------------
        // Guardar usuario
        // -----------------------------------------------------

        return usuarioRepositorio.save(usuario);
    }


    // =========================================================
    // CREAR ADMIN
    // Se utiliza internamente / DataInitializer
    // =========================================================

    public Usuario crearAdmin(
            String username,
            String password) {

        if (usuarioRepositorio
                .existsByUsername(username)) {

            throw new RuntimeException(
                "El nombre de usuario ya existe"
            );
        }


        Usuario usuario = new Usuario();

        usuario.setUsername(username);

        usuario.setPassword(
            passwordEncoder.encode(password)
        );

        usuario.setRol(Rol.ADMIN);

        usuario.setActivo(true);

        // ADMIN puede existir sin cliente
        usuario.setCliente(null);


        return usuarioRepositorio.save(usuario);
    }


    // =========================================================
    // CAMBIAR ESTADO
    // =========================================================

    public Usuario cambiarEstado(
            Long id,
            boolean activo) {

        Usuario usuario = buscarPorId(id);

        usuario.setActivo(activo);

        return usuarioRepositorio.save(usuario);
    }


    // =========================================================
    // CAMBIAR ROL
    // =========================================================

    public Usuario cambiarRol(
            Long id,
            Rol rol) {

        if (rol == null) {

            throw new RuntimeException(
                "El rol es obligatorio"
            );
        }

        Usuario usuario = buscarPorId(id);

        usuario.setRol(rol);

        return usuarioRepositorio.save(usuario);
    }


    // =========================================================
    // ASOCIAR CLIENTE
    // =========================================================

    public Usuario asociarCliente(
            Long usuarioId,
            Long clienteId) {

        Usuario usuario = buscarPorId(usuarioId);


        Cliente cliente =
                clienteRepositorio
                    .findById(clienteId)
                    .orElseThrow(() ->
                        new RuntimeException(
                            "Cliente no encontrado con ID: "
                            + clienteId
                        )
                    );


        // Verificar si el cliente ya tiene usuario
        if (usuarioRepositorio
                .existsByClienteId(clienteId)) {

            // Permitir si ya está asociado al mismo usuario
            Usuario usuarioExistente =
                    usuarioRepositorio
                        .findByClienteId(clienteId)
                        .orElse(null);

            if (usuarioExistente != null &&
                !usuarioExistente.getId()
                    .equals(usuarioId)) {

                throw new RuntimeException(
                    "El cliente ya tiene un usuario asociado"
                );
            }
        }


        usuario.setCliente(cliente);

        return usuarioRepositorio.save(usuario);
    }


    // =========================================================
    // DESVINCULAR CLIENTE
    // =========================================================

    public Usuario desvincularCliente(
            Long usuarioId) {

        Usuario usuario = buscarPorId(usuarioId);

        usuario.setCliente(null);

        return usuarioRepositorio.save(usuario);
    }


    // =========================================================
    // ELIMINAR USUARIO
    // =========================================================

    public void eliminarUsuario(Long id) {

        Usuario usuario = buscarPorId(id);

        usuarioRepositorio.delete(usuario);
    }
    public Usuario actualizarPerfil(
            Long id,
            Usuario datos
    ) {

        Usuario usuario =
                usuarioRepositorio
                        .findById(id)
                        .orElseThrow(() ->
                            new RuntimeException(
                                "Usuario no encontrado"
                            )
                        );

        if (datos.getUsername() != null &&
            !datos.getUsername().trim().isEmpty()) {

            String nuevoUsername =
                    datos.getUsername().trim();

            if (!nuevoUsername.equals(
                    usuario.getUsername())) {

                if (usuarioRepositorio
                        .existsByUsername(nuevoUsername)) {

                    throw new RuntimeException(
                        "El nombre de usuario ya existe"
                    );
                }

                usuario.setUsername(
                    nuevoUsername
                );
            }
        }

        if (datos.getPassword() != null &&
            !datos.getPassword().trim().isEmpty()) {
            usuario.setPassword(
                passwordEncoder.encode(
                    datos.getPassword()
                )
            );
        }

        if (usuario.getCliente() != null &&
            datos.getCliente() != null) {

            if (datos.getCliente().getNombre() != null) {

                usuario.getCliente().setNombre(
                    datos.getCliente().getNombre()
                );
            }

            if (datos.getCliente().getDireccion() != null) {

                usuario.getCliente().setDireccion(
                    datos.getCliente().getDireccion()
                );
            }

            if (datos.getCliente().getTelefono() != null) {

                usuario.getCliente().setTelefono(
                    datos.getCliente().getTelefono()
                );
            }

            if (datos.getCliente().getCorreo() != null) {

                usuario.getCliente().setCorreo(
                    datos.getCliente().getCorreo()
                );
            }
        }
        return usuarioRepositorio.save(usuario);
    }
}
