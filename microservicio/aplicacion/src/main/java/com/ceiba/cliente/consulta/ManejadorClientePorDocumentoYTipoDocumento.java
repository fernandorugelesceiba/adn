package com.ceiba.cliente.consulta;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ceiba.cliente.modelo.dto.DtoCliente;
import com.ceiba.cliente.puerto.dao.DaoCliente;

@Component
public class ManejadorClientePorDocumentoYTipoDocumento {

	private final DaoCliente daoCliente;

	public ManejadorClientePorDocumentoYTipoDocumento(DaoCliente daoCliente){
        this.daoCliente = daoCliente;
    }

	public List<DtoCliente> ejecutar(Short tipoDocumento, String numeroDocumento){
    	return this.daoCliente.obtenerClienteSegunTipoYNumeroDocumento(tipoDocumento, numeroDocumento); 
    }
}
