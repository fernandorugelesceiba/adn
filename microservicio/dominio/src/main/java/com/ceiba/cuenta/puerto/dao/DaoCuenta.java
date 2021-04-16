package com.ceiba.cuenta.puerto.dao;

import java.util.List;

import com.ceiba.cuenta.modelo.dto.DtoCuenta;

public interface DaoCuenta {

    /**
     * Permite listar cuentas por cliente
     * @return los clintes
     */
    List<DtoCuenta> listar();
    
    
    /**
     * Permite listar cuentas por cliente
     * @return las cuentas
     */
    List<DtoCuenta> listarCuentasPorCliente(Long cliente);
}
