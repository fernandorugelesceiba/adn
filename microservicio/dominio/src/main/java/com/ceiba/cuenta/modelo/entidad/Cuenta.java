package com.ceiba.cuenta.modelo.entidad;

import lombok.Getter;

import java.time.LocalDateTime;

import static com.ceiba.dominio.ValidadorArgumento.validarLongitud;
import static com.ceiba.dominio.ValidadorArgumento.validarObligatorio;

@Getter
public class Cuenta {

	private static final String SE_DEBE_INGRESAR_UN_NUMERO_DE_CUENTA = "Se debe ingresar un numero de cuenta";
	private static final String SE_DEBE_INGRESAR_UN_MONTO_MAXIMO = "Se debe ingresar un monto maximo";
	private static final String SE_DEBE_INGRESAR_UN_MONTO = "Se debe ingresar un monto";
	private static final String SE_DEBE_INGRESAR_LA_FECHA_CREACION = "Se debe ingresar la fecha de creación";
	private static final String SE_DEBE_INGRESAR_UN_ID_CLIENTE = "Se debe ingresar un id de un cliente";

	private Long id;
	private String numeroCuenta;
	private Double montoMaximo;
	private Double monto;
	private Long idCliente;
	private LocalDateTime fechaCreacion;

	public Cuenta(Long id, String numeroCuenta, Double montoMaximo, Double monto, Long idCliente, LocalDateTime fechaCreacion) {
		validarObligatorio(numeroCuenta, SE_DEBE_INGRESAR_UN_NUMERO_DE_CUENTA);
		validarObligatorio(montoMaximo, SE_DEBE_INGRESAR_UN_MONTO_MAXIMO);
		validarObligatorio(monto, SE_DEBE_INGRESAR_UN_MONTO);
		validarObligatorio(fechaCreacion, SE_DEBE_INGRESAR_LA_FECHA_CREACION);
		validarObligatorio(idCliente, SE_DEBE_INGRESAR_UN_ID_CLIENTE);

		this.id = id;
		this.numeroCuenta =numeroCuenta;
		this.montoMaximo = montoMaximo;
		this.monto = monto;
		this.fechaCreacion = fechaCreacion;
		this.idCliente = idCliente;
	}

}
