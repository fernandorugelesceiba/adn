package com.ceiba.transaccion.modelo.entidad;

import static com.ceiba.dominio.ValidadorArgumento.validarObligatorio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ceiba.dominio.excepcion.ExcepcionDuplicidad;
import lombok.Getter;

@Getter
public class Transaccion {

	private static final Double MONTO_MAXIMO_INDICADO_POR_ENTIDAD = 2000000D;
	private static final Double CANTIDAD_MAXIMA_DE_TRANSACCIONES_POR_MES = 4D;
	private static final Double VALOR_PORCENTAJE_DE_DESCUENTO_DE_UNO_PORCIENTO = 1D;
	private static final Double VALOR_PORCENTAJE_DE_DESCUENTO_DE_CER0_COMA_CINCO_PORCIENTO = 0.5;
	private static final String SE_DEBE_INGRESAR_UNA_CUENTA_DE_ORIGEN = "Se debe ingresar un n�mero de cuenta de origen";
	private static final String SE_DEBE_INGRESAR_UNA_CUENTA_DE_DESTINO = "Se debe ingresar un n�mero de cuenta de destino";
	private static final String SE_DEBE_INGRESAR_EL_VALOR_DE_LA_TRANSACCION = "Se debe ingresar el valor de la transacci�n";
	private static final String SE_DEBE_INGRESAR_EL_VALOR_DEL_PORCENTAJE_DESCONTADO = "Se debe ingresar el valor del porcentaje descontado";
	private static final String SE_DEBE_INGRESAR_LA_FECHA_DE_CREACION = "Se debe ingresar la fecha de creaci�n";
	private static final String SE_DEBE_INGRESAR_EL_ESTADO_DE_LA_TRANSACCION = "Se debe ingresar la transacci�n del estado";
	private static final String UNO_DE_LOS_VALORES_RESULTO_NEGATIVO_NO_POSIBLE_CONTINUAR_CON_TRANSACCION = "Uno de los saldos resulto negativo, no se puede continuar con el proceso";
	private static final String SUS_TRANSACCIONES_SUPERAN_EL_MONTO_MAXIMO_ESTABLECIDO_POR_LA_ENTIDAD = "El total de sus transacciones en este mes, superan el monto permitido impuesto por la entidad";

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

	public void verificarQueSaltoTotalDeTransaccionNoSupreLimiteDelMes(Double valorTotalDeTransacciones) {
		if (valorTotalDeTransacciones > MONTO_MAXIMO_INDICADO_POR_ENTIDAD) {
			throw new ExcepcionDuplicidad(SUS_TRANSACCIONES_SUPERAN_EL_MONTO_MAXIMO_ESTABLECIDO_POR_LA_ENTIDAD);
		}
	}

	public void verificarSaldosNegativos(Double nuevoMontoParaCuentaDestino, Double nuevoMontoParaCuentaOrigen){
		if (nuevoMontoParaCuentaDestino < 0 || nuevoMontoParaCuentaOrigen < 0) {
			throw new ExcepcionDuplicidad(UNO_DE_LOS_VALORES_RESULTO_NEGATIVO_NO_POSIBLE_CONTINUAR_CON_TRANSACCION);
		}
	}

	public BigDecimal conocerValorPorcentajeDeTransaccion(List<Double> cantidadTransaccionesEnTreintaDias){
		if (cantidadTransaccionesEnTreintaDias.size() > CANTIDAD_MAXIMA_DE_TRANSACCIONES_POR_MES) {
			return BigDecimal.valueOf(VALOR_PORCENTAJE_DE_DESCUENTO_DE_UNO_PORCIENTO);
		} else {
			return BigDecimal.valueOf(VALOR_PORCENTAJE_DE_DESCUENTO_DE_CER0_COMA_CINCO_PORCIENTO);
		}
	}

	public Transaccion conNuevoMontoYNuevoPorcentajeTransaccion(Double valorMonto, Double valorPorcentajeMonto) {
		this.valorTransaccion = valorMonto;
		this.porcentajeDescuento = valorPorcentajeMonto;
		return this;
	}
}
