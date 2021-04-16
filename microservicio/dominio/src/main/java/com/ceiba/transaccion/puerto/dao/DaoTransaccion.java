package com.ceiba.transaccion.puerto.dao;

import java.util.List;

import com.ceiba.transaccion.modelo.dto.DtoTransaccion;

public interface DaoTransaccion {

    /**
     * Permite listar transacciones
     * @return transacciones
     */
    List<DtoTransaccion> listar(Long idCuenta);
}
