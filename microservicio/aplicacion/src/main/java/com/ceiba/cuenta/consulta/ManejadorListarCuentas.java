package com.ceiba.cuenta.consulta;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ceiba.cuenta.modelo.dto.DtoCuenta;
import com.ceiba.cuenta.puerto.dao.DaoCuenta;

@Component
public class ManejadorListarCuentas {

	private final DaoCuenta daoCuenta;

	public ManejadorListarCuentas(DaoCuenta daoCuenta) {
		this.daoCuenta = daoCuenta;
	}

	public List<DtoCuenta> ejecutar() {
		return this.daoCuenta.listar();
	}
}
