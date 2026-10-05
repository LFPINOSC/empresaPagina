package com.api.empresa.DTO;

import com.api.empresa.Entidades.Cliente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    // =====================================================
    // DATOS DE ACCESO
    // =====================================================

    @NotBlank(message = "El usuario es obligatorio")
    @Size(
        min = 4,
        max = 50,
        message = "El usuario debe tener entre 4 y 50 caracteres"
    )
    private String username;


    @NotBlank(message = "La contraseña es obligatoria")
    @Size(
        min = 6,
        max = 100,
        message = "La contraseña debe tener al menos 6 caracteres"
    )
    private String password;


    // =====================================================
    // DATOS DEL CLIENTE
    // =====================================================

    @NotNull(message = "Los datos del cliente son obligatorios")
    @Valid
    private Cliente cliente;
}
