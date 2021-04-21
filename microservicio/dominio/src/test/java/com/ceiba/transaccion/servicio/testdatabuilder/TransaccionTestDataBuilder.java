package com.ceiba.transaccion.servicio.testdatabuilder;

import com.ceiba.transaccion.modelo.entidad.Transaccion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransaccionTestDataBuilder {
	private static final String FORMATO_FECHA =  "yyyy-MM-dd HH:mm";

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

	public TransaccionTestDataBuilder conFechaValida(String fecha) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATO_FECHA);
		this.fechaCreacion = LocalDateTime.parse(fecha, formatter);
		return this;
	}

	public TransaccionTestDataBuilder conValorTransaccion(Double valorTransaccion) {
		this.valorTransaccion = valorTransaccion;
		return this;
	}
}
