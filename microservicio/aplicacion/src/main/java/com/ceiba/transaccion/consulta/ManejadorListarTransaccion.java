package com.ceiba.transaccion.consulta;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ceiba.transaccion.modelo.dto.DtoTransaccion;
import com.ceiba.transaccion.puerto.dao.DaoTransaccion;

@Component
public class ManejadorListarTransaccion {

    private final DaoTransaccion daoTransaccion;

    public ManejadorListarTransaccion(DaoTransaccion daoTransaccion){
        this.daoTransaccion = daoTransaccion;
    }

    public List<DtoTransaccion> ejecutar(Long idCuenta){ return this.daoTransaccion.listar(idCuenta); }
}
