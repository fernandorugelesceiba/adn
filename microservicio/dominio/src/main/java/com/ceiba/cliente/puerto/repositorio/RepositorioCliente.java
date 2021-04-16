package com.ceiba.cliente.puerto.repositorio;

import com.ceiba.cliente.modelo.entidad.Cliente;

public interface RepositorioCliente {
    /**
     * Permite crear un cliente
     * @param cliente
     * @return el id generado
     */
    Long crear(Cliente cliente);

    /**
     * Permite actualizar un cliente
     * @param cliente
     */
    void actualizar(Cliente cliente);

    /**
     * Permite eliminar un cliente
     * @param id
     */
    void eliminar(Long id);

    /**
     * Permite validar si existe un cliente con un nombre
     * @param numeroDocumento
     * @param tipoDocumento
     * @return si existe o no
     */
    boolean existe(String numeroDocumento, Short tipoDocumento);
}
