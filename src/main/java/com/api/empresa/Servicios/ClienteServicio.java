package com.api.empresa.Servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.empresa.Entidades.Cliente;
import com.api.empresa.Excepciones.RecursoNoEncontradoException;
import com.api.empresa.Repositorios.ClienteRepositoria;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositoria clienteRepositoria;

    public Cliente guardarCliente(Cliente cliente) {

        // El ID lo genera PostgreSQL
        cliente.setId(null);

        return clienteRepositoria.save(cliente);
    }

    public Cliente obtenerClientePorId(Long id) {

        return clienteRepositoria.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe un cliente con el ID: " + id
                        )
                );
    }

    public void eliminarCliente(Long id) {

        if (!clienteRepositoria.existsById(id)) {

            throw new RecursoNoEncontradoException(
                    "No existe un cliente con el ID: " + id
            );
        }

        clienteRepositoria.deleteById(id);
    }

    public Cliente actualizarCliente(
            Long id,
            Cliente clienteActualizado) {

        Cliente clienteExistente =
                clienteRepositoria.findById(id)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException(
                                        "No existe un cliente con el ID: " + id
                                )
                        );

        clienteExistente.setCedula(
                clienteActualizado.getCedula()
        );

        clienteExistente.setNombre(
                clienteActualizado.getNombre()
        );

        clienteExistente.setApellido(
                clienteActualizado.getApellido()
        );

        clienteExistente.setCorreo(
                clienteActualizado.getCorreo()
        );

        clienteExistente.setDireccion(
                clienteActualizado.getDireccion()
        );

        clienteExistente.setTelefono(
                clienteActualizado.getTelefono()
        );

        return clienteRepositoria.save(clienteExistente);
    }

    public List<Cliente> obtenerTodosLosClientes() {

        return clienteRepositoria.findAll();
    }


    public Cliente obtenerClientePorCedula(String cedula) {

        Cliente cliente =
                clienteRepositoria.findByCedula(cedula);

        if (cliente == null) {

            throw new RecursoNoEncontradoException(
                    "No existe un cliente con la cédula: " + cedula
            );
        }

        return cliente;
    }
    public List<Cliente> obtenerClientesPorDireccion(
            String direccion) {
        return clienteRepositoria.findAllByDireccion(direccion);
    }
}