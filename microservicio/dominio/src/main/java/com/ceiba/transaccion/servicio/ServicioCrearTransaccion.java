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
	private static final String EL_VALOR_DE_LAS_TRANSACCIONES_REALIZADAS_EN_ESTE_MES_SUPERAN_EL_MONTO_MAXIMO_DE_LA_CUENTA = "El monto total de transacciones supera el monto maximo permito";
	private static final String OCURRIO_UN_ERROR_DURANTE_EL_PROCESO_DE_ACTUALIZACION = "Ocurrio un error durante el proceso de actualiación";
	private static final String UNO_DE_LOS_VALORES_RESULTO_NEGATIVO_NO_POSIBLE_CONTINUAR_CON_TRANSACCION = "Uno de los saldos resulto negativo, no se puede continuar con el proceso";
	private static final String SUS_TRANSACCIONES_SUPERAN_EL_MONTO_MAXIMO_ESTABLECIDO_POR_LA_ENTIDAD = "El total de sus transacciones en este mes, superan el monto permitido impuesto por la entidad";

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

	protected void verficarQueLaCuentaLleveUnDiaDeCreada(Long idCuenta) {
		boolean esValido = this.repositorioTransaccion.verificarFechaValidesEnCuenta(idCuenta);
		if (!esValido) {
			throw new ExcepcionDuplicidad(LA_TRANSACCION_NO_SE_REALIZA_POR_CUENTA_RECIEN_CREADA);
		}
	}

	protected void verificarMontoTotalDeLasTransaccionesSegunELMontoMaximoDeLaCuenta(Long idCuentaDestino,
			Double montoTotalTransaccion) {
		Double montoMaximoPermitido = this.repositorioTransaccion
				.obtenerElMontoMaximoDeUnCuentaSegunSuId(idCuentaDestino);
		if (montoTotalTransaccion > montoMaximoPermitido) {
			throw new ExcepcionDuplicidad(
					EL_VALOR_DE_LAS_TRANSACCIONES_REALIZADAS_EN_ESTE_MES_SUPERAN_EL_MONTO_MAXIMO_DE_LA_CUENTA);
		}
	}

	protected Transaccion crearValorPorcentajeDeTransaccion(Transaccion transaccion) {
		List<Double> cantidadTransaccionesEnTreintaDias = this
				.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(transaccion.getIdCuentaOrigen());
		BigDecimal valorProcentanjeTransacciones;
		BigDecimal valorTotalDeTransacciones;
		BigDecimal nuevoValorDelMonto;

		valorTotalDeTransacciones = cantidadTransaccionesEnTreintaDias.isEmpty()
				? new BigDecimal(0)
				: BigDecimal.valueOf(cantidadTransaccionesEnTreintaDias.stream().flatMapToDouble(DoubleStream::of).sum());

		this.verificarQueSaltoTotalDETransaccionNoSupreLimiteDelMes(valorTotalDeTransacciones.doubleValue());

		valorTotalDeTransacciones = valorTotalDeTransacciones.add(BigDecimal.valueOf(transaccion.getValorTransaccion()));

		valorProcentanjeTransacciones = this.conocerValorPorcentajeDeTransaccion(cantidadTransaccionesEnTreintaDias);

		BigDecimal valorADescontar = BigDecimal.valueOf(transaccion.getValorTransaccion())
				.multiply(valorProcentanjeTransacciones).divide(new BigDecimal(100));
		nuevoValorDelMonto = BigDecimal.valueOf(transaccion.getValorTransaccion()).subtract(valorADescontar);
		this.verificarMontoTotalDeLasTransaccionesSegunELMontoMaximoDeLaCuenta(transaccion.getIdCuentaOrigen(),
				valorTotalDeTransacciones.doubleValue());

		return new Transaccion(transaccion.getId(),
				transaccion.getIdCuentaOrigen(), transaccion.getIdCuentaDestino(), nuevoValorDelMonto.doubleValue(),
				valorProcentanjeTransacciones.doubleValue(), transaccion.getFechaCreacion(), transaccion.getEstado());
	}

	protected List<Double> obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(Long idCuenta) {
		return this.repositorioTransaccion.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(idCuenta);
	}

	protected void ejecutarActualizacionDeMontosEnCuentas(Transaccion transaccion,
			Transaccion transaccionConValorProcentajeDeTransaccion) {
		try {
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
		}catch (NullPointerException e){
			throw new NullPointerException(OCURRIO_UN_ERROR_DURANTE_EL_PROCESO_DE_ACTUALIZACION);
		}
	}

	protected void verificarSaldosNegativos(Double nuevoMontoParaCuentaDestino, Double nuevoMontoParaCuentaOrigen) {
		if (nuevoMontoParaCuentaDestino < 0 || nuevoMontoParaCuentaOrigen < 0) {
			throw new ExcepcionDuplicidad(UNO_DE_LOS_VALORES_RESULTO_NEGATIVO_NO_POSIBLE_CONTINUAR_CON_TRANSACCION);
		}
	}

	protected void verificarQueSaltoTotalDETransaccionNoSupreLimiteDelMes(Double valorTotalDeTransacciones) {
		if (valorTotalDeTransacciones > 2000000) {
			throw new ExcepcionDuplicidad(SUS_TRANSACCIONES_SUPERAN_EL_MONTO_MAXIMO_ESTABLECIDO_POR_LA_ENTIDAD);
		}
	}

	protected BigDecimal conocerValorPorcentajeDeTransaccion(List<Double> cantidadTransaccionesEnTreintaDias){
		if (cantidadTransaccionesEnTreintaDias.size() > 4) {
			return BigDecimal.valueOf(1);
		} else {
			return BigDecimal.valueOf(0.5);
		}
	}
}
