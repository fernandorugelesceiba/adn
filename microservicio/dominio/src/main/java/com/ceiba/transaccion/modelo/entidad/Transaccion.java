package com.ceiba.transaccion.modelo.entidad;

import static com.ceiba.dominio.ValidadorArgumento.validarObligatorio;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class Transaccion {

	private static final String SE_DEBE_INGRESAR_UNA_CUENTA_DE_ORIGEN = "Se debe ingresar un número de cuenta de origen";
	private static final String SE_DEBE_INGRESAR_UNA_CUENTA_DE_DESTINO = "Se debe ingresar un número de cuenta de destino";
	private static final String SE_DEBE_INGRESAR_EL_VALOR_DE_LA_TRANSACCION = "Se debe ingresar el valor de la transacción";
	private static final String SE_DEBE_INGRESAR_EL_VALOR_DEL_PORCENTAJE_DESCONTADO = "Se debe ingresar el valor del porcentaje descontado";
	private static final String SE_DEBE_INGRESAR_LA_FECHA_DE_CREACION = "Se debe ingresar la fecha de creación";
	private static final String SE_DEBE_INGRESAR_EL_ESTADO_DE_LA_TRANSACCION = "Se debe ingresar la transacción del estado";

	private Long id;
	private Long idCuentaOrigen;
	private Long idCuentaDestino;
	private Double valorTransaccion;
	private Double porcentajeDescuento;
	private LocalDateTime fechaCreacion;
	private Short estado;

	public Transaccion(Long id, Long idCuentaOrigen, Long idCuentaDestino, Double valorTransaccion, Double porcentajeDescuento,
			LocalDateTime fechaCreacion, Short estado) {
		validarObligatorio(idCuentaOrigen, SE_DEBE_INGRESAR_UNA_CUENTA_DE_ORIGEN);
		validarObligatorio(idCuentaDestino, SE_DEBE_INGRESAR_UNA_CUENTA_DE_DESTINO);
		validarObligatorio(valorTransaccion, SE_DEBE_INGRESAR_EL_VALOR_DE_LA_TRANSACCION);
		validarObligatorio(porcentajeDescuento, SE_DEBE_INGRESAR_EL_VALOR_DEL_PORCENTAJE_DESCONTADO);
		validarObligatorio(fechaCreacion, SE_DEBE_INGRESAR_LA_FECHA_DE_CREACION);
		validarObligatorio(estado, SE_DEBE_INGRESAR_EL_ESTADO_DE_LA_TRANSACCION);

		this.id = id;
		this.idCuentaOrigen = idCuentaOrigen;
		this.idCuentaDestino = idCuentaDestino;
		this.valorTransaccion = valorTransaccion;
		this.porcentajeDescuento = porcentajeDescuento;
		this.fechaCreacion = fechaCreacion;
		this.estado = estado;
		
	}

}
