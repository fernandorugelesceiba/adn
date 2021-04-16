package com.ceiba.transaccion.servicio;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.DoubleStream;

import com.ceiba.cuenta.modelo.dto.DtoCuenta;
import com.ceiba.cuenta.modelo.entidad.Cuenta;
import com.ceiba.cuenta.puerto.repositorio.RepositorioCuenta;
import com.ceiba.dominio.excepcion.ExcepcionDuplicidad;
import com.ceiba.transaccion.modelo.entidad.Transaccion;
import com.ceiba.transaccion.puerto.repositorio.RepositorioTransaccion;

public class ServicioCrearTransaccion {
	private static final String LA_TRANSACCION_NO_SE_REALIZA_POR_CUENTA_RECIEN_CREADA = "Lo sentimos por el momento no se pueden realizar transacciones, las cuentas deben llevar mas de un dia de creadas ya que por temas de validacion la entidad lo exige";
	private static final String SE_ENCONTRO_QUE_EL_MONTO_TRANSFERIDO_SUPERA_EL_MONTO_EN_SU_CUENTA = "se encontrÓ que el monto transferido supera el monto en su cuenta";
	private static final String EL_VALOR_DE_LAS_TRANSACCIONES_REALIZADAS_EN_ESTE_MES_SUPERAN_EL_MONTO_MAXIMO_DE_LA_CUENTA = "El monto total de transacciones supera el monto maximo permito";

	private final RepositorioTransaccion repositorioTransaccion;
	private final RepositorioCuenta repositorioCuenta;

	public ServicioCrearTransaccion(RepositorioTransaccion repositorioTransaccion,
			RepositorioCuenta repositorioCuenta) {
		this.repositorioTransaccion = repositorioTransaccion;
		this.repositorioCuenta = repositorioCuenta;
	}

	public Long ejecutar(Transaccion transaccion) {
		this.verficarQueLaCuentaLleveUnDiaDeCreada(transaccion.getIdCuentaOrigen());
		this.verficarQueLaCuentaLleveUnDiaDeCreada(transaccion.getIdCuentaDestino());

		Transaccion transaccionConValorProcentajeDeTransaccion = this.crearValorPorcentajeDeTransaccion(transaccion);

		Long idTransaccion = this.repositorioTransaccion.crear(transaccionConValorProcentajeDeTransaccion);
		// actualizar los nuevos montos de los saldos
		this.ejecutarActualizacionDeMontosEnCuentas(transaccion, transaccionConValorProcentajeDeTransaccion);

		return idTransaccion;
	}

	private void verficarQueLaCuentaLleveUnDiaDeCreada(Long idCuenta) {
		boolean esValido = this.repositorioTransaccion.verificarFechaValidesEnCuenta(idCuenta);
		if (!esValido) {
			throw new ExcepcionDuplicidad(LA_TRANSACCION_NO_SE_REALIZA_POR_CUENTA_RECIEN_CREADA);
		}
	}

	private void verificarMontoTotalDeLasTransaccionesSegunELMontoMaximoDeLaCuenta(Long idCuentaDestino,
			Double montoTotalTransaccion) {
		Double montoMaximoPermitido = this.repositorioTransaccion
				.obtenerElMontoMaximoDeUnCuentaSegunSuId(idCuentaDestino);
		if (montoTotalTransaccion > montoMaximoPermitido) {
			throw new ExcepcionDuplicidad(
					EL_VALOR_DE_LAS_TRANSACCIONES_REALIZADAS_EN_ESTE_MES_SUPERAN_EL_MONTO_MAXIMO_DE_LA_CUENTA);
		}
	}

	private Transaccion crearValorPorcentajeDeTransaccion(Transaccion transaccion) {
		List<Double> cantidadTransaccionesEnTreintaDias = this
				.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(transaccion.getIdCuentaOrigen());
		BigDecimal valorProcentanjeTransacciones = new BigDecimal(0);
		BigDecimal valorTotalDeTransacciones = new BigDecimal(0);
		BigDecimal nuevoValorDelMonto = new BigDecimal(0);

		valorTotalDeTransacciones = cantidadTransaccionesEnTreintaDias.size() > 0
				? new BigDecimal(cantidadTransaccionesEnTreintaDias.stream().flatMapToDouble(DoubleStream::of).sum())
				: new BigDecimal(0);

		valorTotalDeTransacciones = valorTotalDeTransacciones.add(new BigDecimal(transaccion.getValorTransaccion()));

		if (cantidadTransaccionesEnTreintaDias.size() > 5) {
			valorProcentanjeTransacciones = new BigDecimal(1);
		} else {
			valorProcentanjeTransacciones = new BigDecimal(0.5);
		}

		BigDecimal valorADescontar = new BigDecimal(transaccion.getValorTransaccion())
				.multiply(valorProcentanjeTransacciones).divide(new BigDecimal(100));
		nuevoValorDelMonto = new BigDecimal(transaccion.getValorTransaccion()).subtract(valorADescontar);
		this.verificarMontoTotalDeLasTransaccionesSegunELMontoMaximoDeLaCuenta(transaccion.getIdCuentaOrigen(),
				valorTotalDeTransacciones.doubleValue());

		Transaccion transaccionConValorProcentajeDeTransaccion = new Transaccion(transaccion.getId(),
				transaccion.getIdCuentaOrigen(), transaccion.getIdCuentaDestino(), nuevoValorDelMonto.doubleValue(),
				valorProcentanjeTransacciones.doubleValue(), transaccion.getFechaCreacion(), transaccion.getEstado());

		return transaccionConValorProcentajeDeTransaccion;
	}

	private List<Double> obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(Long idCuenta) {
		return this.repositorioTransaccion.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(idCuenta);
	}

	private void ejecutarActualizacionDeMontosEnCuentas(Transaccion transaccion,
			Transaccion transaccionConValorProcentajeDeTransaccion) {

		// obtener informacion de las cuentas a modificar
		DtoCuenta cuentaDestino = this.repositorioCuenta.obtenerCuentaSegunId(transaccion.getIdCuentaDestino()).get(0);
		DtoCuenta cuentaOrigen = this.repositorioCuenta.obtenerCuentaSegunId(transaccion.getIdCuentaOrigen()).get(0);

		Double nuevoMontoParaCuentaDestino = cuentaDestino.getMonto()
				+ transaccionConValorProcentajeDeTransaccion.getValorTransaccion();
		Double nuevoMontoParaCuentaOrigen = cuentaOrigen.getMonto()
				- transaccionConValorProcentajeDeTransaccion.getValorTransaccion();

		// no descontar si hay negativos en los saldos
		this.verificarSaldosNegativos(nuevoMontoParaCuentaDestino, nuevoMontoParaCuentaOrigen);

		Cuenta cuentaDestinoFabricada = new Cuenta(cuentaDestino.getId(), cuentaDestino.getNumeroCuenta(),
				cuentaDestino.getMontoMaximo(), nuevoMontoParaCuentaDestino, cuentaDestino.getIdCliente(),
				cuentaDestino.getFechaCreacion());

		Cuenta cuentaOrigenFabricada = new Cuenta(cuentaOrigen.getId(), cuentaOrigen.getNumeroCuenta(),
				cuentaOrigen.getMontoMaximo(), nuevoMontoParaCuentaOrigen, cuentaOrigen.getIdCliente(),
				cuentaOrigen.getFechaCreacion());
		this.repositorioCuenta.actualizar(cuentaDestinoFabricada);
		this.repositorioCuenta.actualizar(cuentaOrigenFabricada);
	}

	private void verificarSaldosNegativos(Double nuevoMontoParaCuentaDestino, Double nuevoMontoParaCuentaOrigen) {
		if (nuevoMontoParaCuentaDestino < 0 || nuevoMontoParaCuentaOrigen < 0) {
			throw new ExcepcionDuplicidad(SE_ENCONTRO_QUE_EL_MONTO_TRANSFERIDO_SUPERA_EL_MONTO_EN_SU_CUENTA);
		}
	}
}
