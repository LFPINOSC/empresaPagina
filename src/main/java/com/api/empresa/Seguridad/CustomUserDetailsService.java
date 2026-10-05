package com.api.empresa.Seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.api.empresa.Entidades.Usuario;
import com.api.empresa.Repositorios.UsuarioRepositorio;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {

        Usuario usuario =
                usuarioRepositorio
                    .findByUsername(username)
                    .orElseThrow(() ->
                        new UsernameNotFoundException(
                            "Usuario no encontrado: " + username
                        )
                    );

        return User.builder() .username(usuario.getUsername()) .password(usuario.getPassword()) .roles(usuario.getRol().name()) .build();
    }
}