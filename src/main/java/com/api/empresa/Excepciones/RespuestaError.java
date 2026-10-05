package com.api.empresa.Excepciones;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaError {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String mensaje;

    private String path;

    private Map<String, String> errores;
}