package com.ceiba.transaccion.servicio;

import com.ceiba.cuenta.modelo.dto.DtoCuenta;
import com.ceiba.cuenta.modelo.entidad.Cuenta;
import com.ceiba.cuenta.puerto.repositorio.RepositorioCuenta;
import com.ceiba.dominio.excepcion.ExcepcionDuplicidad;
import com.ceiba.transaccion.modelo.entidad.Transaccion;
import com.ceiba.transaccion.puerto.repositorio.RepositorioTransaccion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.DoubleStream;

public class ServicioCrearTransaccion {
	private static final String LA_TRANSACCION_NO_SE_REALIZA_POR_CUENTA_RECIEN_CREADA = "Lo sentimos por el momento no se pueden realizar transacciones, las cuentas deben llevar mas de un dia de creadas ya que por temas de validacion la entidad lo exige";
	private static final String EL_VALOR_DE_LAS_TRANSACCIONES_REALIZADAS_EN_ESTE_MES_SUPERAN_EL_MONTO_MAXIMO_DE_LA_CUENTA = "El monto total de transacciones supera el monto maximo permito";
	private static final String OCURRIO_UN_ERROR_DURANTE_EL_PROCESO_DE_ACTUALIZACION = "Ocurrio un error durante el proceso de actualiación";

	private final RepositorioTransaccion repositorioTransaccion;
	private final RepositorioCuenta repositorioCuenta;

	public ServicioCrearTransaccion(
			RepositorioTransaccion repositorioTransaccion,
			RepositorioCuenta repositorioCuenta
	) {
		this.repositorioTransaccion = repositorioTransaccion;
		this.repositorioCuenta = repositorioCuenta;
	}

	public Long ejecutar(Transaccion transaccion) {
		this.verficarQueLaCuentaLleveUnDiaDeCreada(
				transaccion.getIdCuentaOrigen(),
				transaccion.getIdCuentaDestino()
		);

		this.crearValorPorcentajeTransaccion(transaccion);

		Long idTransaccion = this.repositorioTransaccion.crear(transaccion);
		// actualizar los nuevos montos de los saldos
		this.ejecutarActualizacionDeMontosEnCuentas(transaccion);
		return idTransaccion;
	}

	private void crearValorPorcentajeTransaccion(Transaccion transaccion) {
		LocalDateTime fechaInicio = transaccion.getFechaCreacion().with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime fechaFin = transaccion.getFechaCreacion().with(TemporalAdjusters.lastDayOfMonth());

		List<Double> cantidadTransaccionesMesual =
				this.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(
						transaccion.getIdCuentaOrigen(),
						fechaInicio,
						fechaFin
				);

        BigDecimal valorTotalDeTransacciones = BigDecimal.valueOf(
				cantidadTransaccionesMesual.stream()
								.flatMapToDouble(DoubleStream::of).sum());

		transaccion.verificarQueSaltoTotalDeTransaccionNoSupreLimiteDelMes(valorTotalDeTransacciones.doubleValue());

		valorTotalDeTransacciones =
				valorTotalDeTransacciones.add(BigDecimal.valueOf(transaccion.getValorTransaccion()));

        BigDecimal valorProcentanjeTransacciones =
				transaccion.conocerValorPorcentajeDeTransaccion(cantidadTransaccionesMesual);

		BigDecimal valorADescontar = BigDecimal.valueOf(
				transaccion.getValorTransaccion())
				.multiply(valorProcentanjeTransacciones)
				.divide(new BigDecimal(100));

        BigDecimal nuevoValorDelMonto = BigDecimal.valueOf(transaccion.getValorTransaccion()).subtract(valorADescontar);
		this.verificarMontoTotalDeLasTransaccionesSegunELMontoMaximoDeLaCuenta(transaccion.getIdCuentaOrigen(),
				valorTotalDeTransacciones.doubleValue());

		transaccion.conNuevoMontoYNuevoPorcentajeTransaccion(nuevoValorDelMonto.doubleValue(), valorProcentanjeTransacciones.doubleValue());
	}

	private void verficarQueLaCuentaLleveUnDiaDeCreada(Long idCuentaOrigen, Long idCuentaDestino) {
		boolean esValidaParaCuentaOrigen = this.repositorioTransaccion.verificarFechaValidesEnCuenta(idCuentaOrigen);
		boolean esValidaParaCuentaDestino = this.repositorioTransaccion.verificarFechaValidesEnCuenta(idCuentaDestino);
		if (!esValidaParaCuentaOrigen || !esValidaParaCuentaDestino) {
			throw new ExcepcionDuplicidad(LA_TRANSACCION_NO_SE_REALIZA_POR_CUENTA_RECIEN_CREADA);
		}
	}

	private void verificarMontoTotalDeLasTransaccionesSegunELMontoMaximoDeLaCuenta(
			Long idCuentaOrigen,
			Double montoTotalTransaccion
	) {


		Double montoMaximoPermitido =
				this.repositorioTransaccion.obtenerElMontoMaximoDeUnCuentaSegunSuId(idCuentaOrigen);
		if (montoTotalTransaccion > montoMaximoPermitido) {
			throw new ExcepcionDuplicidad(
					EL_VALOR_DE_LAS_TRANSACCIONES_REALIZADAS_EN_ESTE_MES_SUPERAN_EL_MONTO_MAXIMO_DE_LA_CUENTA);
		}
	}



	private List<Double> obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(Long idCuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		return this.repositorioTransaccion.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(idCuenta, fechaInicio, fechaFin);
	}

	private void ejecutarActualizacionDeMontosEnCuentas(Transaccion transaccion) {
		try {
			// obtener informacion de las cuentas a modificar
			DtoCuenta cuentaDestino = this.repositorioCuenta.obtenerCuentaSegunId(transaccion.getIdCuentaDestino()).get(0);
			DtoCuenta cuentaOrigen = this.repositorioCuenta.obtenerCuentaSegunId(transaccion.getIdCuentaOrigen()).get(0);

			Double nuevoMontoParaCuentaDestino = cuentaDestino.getMonto()
					+ transaccion.getValorTransaccion();
			Double nuevoMontoParaCuentaOrigen = cuentaOrigen.getMonto()
					- transaccion.getValorTransaccion();

			// no descontar si hay negativos en los saldos
			transaccion.verificarSaldosNegativos(nuevoMontoParaCuentaDestino, nuevoMontoParaCuentaOrigen);

			Cuenta cuentaDestinoFabricada = new Cuenta(cuentaDestino.getId(), cuentaDestino.getNumeroCuenta(),
					cuentaDestino.getMontoMaximo(), nuevoMontoParaCuentaDestino, cuentaDestino.getIdCliente(),
					cuentaDestino.getFechaCreacion());

			Cuenta cuentaOrigenFabricada = new Cuenta(cuentaOrigen.getId(), cuentaOrigen.getNumeroCuenta(),
					cuentaOrigen.getMontoMaximo(), nuevoMontoParaCuentaOrigen, cuentaOrigen.getIdCliente(),
					cuentaOrigen.getFechaCreacion());
			this.repositorioCuenta.actualizar(cuentaDestinoFabricada);
			this.repositorioCuenta.actualizar(cuentaOrigenFabricada);
		}catch (NullPointerException e){
			throw new NullPointerException(OCURRIO_UN_ERROR_DURANTE_EL_PROCESO_DE_ACTUALIZACION);
		}
	}

}
