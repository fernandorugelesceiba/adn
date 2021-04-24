package com.ceiba.transaccion.modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DtoTransaccion {
	private Long id;
	private Long idCuentaOrigen;
	private Long idCuentaDestino;
	private Double valorTransaccion;
	private Double porcentajeDescuento;
	private LocalDateTime fechaCreacion;
	private Short estado;
	private String numeroCuentaOrigen;
	private String numeroCuentaDestino;
}
