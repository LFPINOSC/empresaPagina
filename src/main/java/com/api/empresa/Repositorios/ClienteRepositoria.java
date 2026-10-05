
package com.api.empresa.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.empresa.Entidades.Cliente;


/**
 * ClienteRepositoria
 */
public interface ClienteRepositoria  extends JpaRepository<Cliente, Long> {
    Cliente findByCedula(String cedula);
    List<Cliente> findAllByDireccion(String direccion);
    boolean existsByCedula(String cedula);
}