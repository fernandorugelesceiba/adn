package com.ceiba.cliente.puerto.dao;

import java.util.List;

import com.ceiba.cliente.modelo.dto.DtoCliente;

public interface DaoCliente {

    /**
     * Permite listar clientes
     * @return los clintes
     */
    List<DtoCliente> listar();
    
    
    /**
     * Permite obtener un cliente
     * @return un cliente
     */
    List<DtoCliente> obtenerClienteSegunTipoYNumeroDocumento(Short tipoDocumento, String numeroDocumento);
}
