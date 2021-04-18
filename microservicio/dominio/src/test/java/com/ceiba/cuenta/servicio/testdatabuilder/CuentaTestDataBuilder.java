package com.ceiba.cuenta.servicio.testdatabuilder;

import java.time.LocalDateTime;

import com.ceiba.cuenta.modelo.dto.DtoCuenta;
import com.ceiba.cuenta.modelo.entidad.Cuenta;

public class CuentaTestDataBuilder {

	private Long id;
	private String numeroCuenta;
	private Double montoMaximo;
	private Double monto;
	private Long idCliente;
	private LocalDateTime fechaCreacion;

    public CuentaTestDataBuilder() {
    	id = 1L;
		numeroCuenta = "120000";
		montoMaximo = 600D;
		monto = 100D;
		idCliente = 1L;
    	fechaCreacion = LocalDateTime.now();
    }

    public Cuenta build() {
        return new Cuenta( id,  numeroCuenta,  montoMaximo,  monto,  idCliente,  fechaCreacion);
    }

	public DtoCuenta buildDto() {
		return new DtoCuenta( id,  numeroCuenta,  montoMaximo,  monto,  idCliente,  fechaCreacion);
	}
}
