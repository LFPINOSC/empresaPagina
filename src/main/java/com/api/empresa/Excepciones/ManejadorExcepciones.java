package com.api.empresa.Excepciones;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<RespuestaError> manejarRecursoNoEncontrado(
            RecursoNoEncontradoException ex,
            WebRequest request) {

        RespuestaError respuesta = new RespuestaError();

        respuesta.setTimestamp(LocalDateTime.now());
        respuesta.setStatus(HttpStatus.NOT_FOUND.value());
        respuesta.setError("NOT_FOUND");
        respuesta.setMensaje(ex.getMessage());
        respuesta.setPath(obtenerPath(request));

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(respuesta);
    }


    // =====================================================
    // ERRORES DE VALIDACIÓN - 400
    // =====================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaError> manejarValidaciones(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {

                    errores.put(
                            error.getField(),
                            error.getDefaultMessage()
                    );

                });

        RespuestaError respuesta = new RespuestaError();

        respuesta.setTimestamp(LocalDateTime.now());
        respuesta.setStatus(HttpStatus.BAD_REQUEST.value());
        respuesta.setError("BAD_REQUEST");
        respuesta.setMensaje("Error de validación");
        respuesta.setPath(obtenerPath(request));

        // IMPORTANTE
        respuesta.setErrores(errores);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(respuesta);
    }


    // =====================================================
    // ARGUMENTO INCORRECTO - 400
    // =====================================================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RespuestaError> manejarArgumentoIncorrecto(
            IllegalArgumentException ex,
            WebRequest request) {

        RespuestaError respuesta = new RespuestaError();

        respuesta.setTimestamp(LocalDateTime.now());
        respuesta.setStatus(HttpStatus.BAD_REQUEST.value());
        respuesta.setError("BAD_REQUEST");
        respuesta.setMensaje(ex.getMessage());
        respuesta.setPath(obtenerPath(request));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(respuesta);
    }


    // =====================================================
    // ERROR DE BASE DE DATOS - 409
    // =====================================================

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RespuestaError> manejarIntegridadDatos(
            DataIntegrityViolationException ex,
            WebRequest request) {

        RespuestaError respuesta = new RespuestaError();

        respuesta.setTimestamp(LocalDateTime.now());
        respuesta.setStatus(HttpStatus.CONFLICT.value());
        respuesta.setError("CONFLICT");
        respuesta.setMensaje(
                "No se puede realizar la operación porque los datos "
                + "violan una restricción de la base de datos"
        );
        respuesta.setPath(obtenerPath(request));

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(respuesta);
    }


    // =====================================================
    // ERROR GENERAL - 500
    // =====================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaError> manejarErrorGeneral(
            Exception ex,
            WebRequest request) {

        // Mostrar el error real en la consola
        ex.printStackTrace();

        RespuestaError respuesta = new RespuestaError();

        respuesta.setTimestamp(LocalDateTime.now());
        respuesta.setStatus(
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        respuesta.setError("INTERNAL_SERVER_ERROR");

        respuesta.setMensaje(
                "Ha ocurrido un error interno en el servidor"
        );

        respuesta.setPath(obtenerPath(request));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(respuesta);
    }


    // =====================================================
    // OBTENER URL DE LA PETICIÓN
    // =====================================================

    private String obtenerPath(WebRequest request) {

        return request
                .getDescription(false)
                .replace("uri=", "");
    }
}