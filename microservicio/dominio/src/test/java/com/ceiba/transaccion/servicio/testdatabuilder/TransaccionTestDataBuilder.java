package com.ceiba.transaccion.servicio.testdatabuilder;

import java.time.LocalDateTime;

import com.ceiba.cuenta.modelo.dto.DtoCuenta;
import com.ceiba.transaccion.modelo.dto.DtoTransaccion;
import com.ceiba.transaccion.modelo.entidad.Transaccion;

public class TransaccionTestDataBuilder {

	private Long id;
	private Long idCuentaOrigen;
	private Long idCuentaDestino;
	private Double valorTransaccion;
	private Double porcentajeDescuento;
	private LocalDateTime fechaCreacion;
	private Short estado;

    public TransaccionTestDataBuilder() {
    	id = 1L;
    	idCuentaOrigen = 1L;
    	idCuentaDestino = 2L;
    	valorTransaccion = 100D;
    	porcentajeDescuento = 0.5;
    	fechaCreacion = LocalDateTime.now();
    	estado = 1;
    }

    public Transaccion build() {
        return new Transaccion(id, idCuentaOrigen, idCuentaDestino, valorTransaccion, porcentajeDescuento, fechaCreacion, estado);
    }
}
