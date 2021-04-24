package com.ceiba.transaccion.servicio;

import com.ceiba.BasePrueba;
import com.ceiba.cuenta.modelo.dto.DtoCuenta;
import com.ceiba.cuenta.modelo.entidad.Cuenta;
import com.ceiba.cuenta.puerto.repositorio.RepositorioCuenta;
import com.ceiba.cuenta.servicio.testdatabuilder.CuentaTestDataBuilder;
import com.ceiba.dominio.excepcion.ExcepcionDuplicidad;
import com.ceiba.transaccion.modelo.entidad.Transaccion;
import com.ceiba.transaccion.puerto.repositorio.RepositorioTransaccion;
import com.ceiba.transaccion.servicio.testdatabuilder.TransaccionTestDataBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class ServicioCrearTransaccionTest {
	private static final String OCURRIO_UN_ERROR_DURANTE_EL_PROCESO_DE_ACTUALIZACION = "Ocurrio un error durante el proceso de actualiación";
	private static final String LA_TRANSACCION_NO_SE_REALIZA_POR_CUENTA_RECIEN_CREADA = "Lo sentimos por el momento no se pueden realizar transacciones, las cuentas deben llevar mas de un dia de creadas ya que por temas de validacion la entidad lo exige";
	private static final String EL_VALOR_DE_LAS_TRANSACCIONES_REALIZADAS_EN_ESTE_MES_SUPERAN_EL_MONTO_MAXIMO_DE_LA_CUENTA = "El monto total de transacciones supera el monto maximo permito";
	private static final String SUS_TRANSACCIONES_SUPERAN_EL_MONTO_MAXIMO_ESTABLECIDO_POR_LA_ENTIDAD = "El total de sus transacciones en este mes, superan el monto permitido impuesto por la entidad";
	private static final String UNO_DE_LOS_VALORES_RESULTO_NEGATIVO_NO_POSIBLE_CONTINUAR_CON_TRANSACCION = "Uno de los saldos resulto negativo, no se puede continuar con el proceso";

	@Test
	public void seEsperaUnFalloVerificandoQueLaCuentaOrigenLleveUnDiaDeCreadaTest() {
		// arrange
		Transaccion transaccion = new TransaccionTestDataBuilder().build();
		RepositorioTransaccion repositorioTransaccionMock = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuentaMock = Mockito.mock(RepositorioCuenta.class);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaOrigen())).thenReturn(false);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaDestino())).thenReturn(true);
		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccionMock,
				repositorioCuentaMock);

		// act - assert
		BasePrueba.assertThrows(() -> servicioCrearTransaccion.ejecutar(transaccion),
				ExcepcionDuplicidad.class, LA_TRANSACCION_NO_SE_REALIZA_POR_CUENTA_RECIEN_CREADA);
	}

	@Test
	public void seEsperaUnFalloVerificandoQueLaCuentaDestinoLleveUnDiaDeCreadaTest() {
		// arrange
		Transaccion transaccion = new TransaccionTestDataBuilder().build();
		RepositorioTransaccion repositorioTransaccionMock = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuentaMock = Mockito.mock(RepositorioCuenta.class);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaOrigen())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaDestino())).thenReturn(false);
		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccionMock,
				repositorioCuentaMock);

		// act - assert
		BasePrueba.assertThrows(() -> servicioCrearTransaccion.ejecutar(transaccion),
				ExcepcionDuplicidad.class, LA_TRANSACCION_NO_SE_REALIZA_POR_CUENTA_RECIEN_CREADA);
	}

	@Test
	public void seExcedeElMontoMaximoEstablecidoPorEntidadEnCreacionDeValorPorcentajeDeTransaccionTest() {
		// arrange
		LocalDateTime fecha = LocalDateTime.now();
		LocalDateTime fechaInicio = fecha.with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime fechaFin = fecha.with(TemporalAdjusters.lastDayOfMonth());
		List<Double> cantidadTransaccionesMesual = new ArrayList<>();
		cantidadTransaccionesMesual.add(1000000D);
		cantidadTransaccionesMesual.add(3000000D);
		Transaccion transaccion = new TransaccionTestDataBuilder().build();
		RepositorioTransaccion repositorioTransaccionMock = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuentaMock = Mockito.mock(RepositorioCuenta.class);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaOrigen())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaDestino())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(transaccion.getIdCuentaOrigen(),fechaInicio,fechaFin)).thenReturn(cantidadTransaccionesMesual);
		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccionMock,
				repositorioCuentaMock);

		// act - assert
		BasePrueba.assertThrows(() -> servicioCrearTransaccion.ejecutar(transaccion),
				ExcepcionDuplicidad.class, SUS_TRANSACCIONES_SUPERAN_EL_MONTO_MAXIMO_ESTABLECIDO_POR_LA_ENTIDAD);
	}

	@Test
	public void seEsperaProcentajeDeDescuentoSegunMaximaCantidadDeTransaccionesEnElMesTest() {
		// arrange
		LocalDateTime fecha = LocalDateTime.now();
		LocalDateTime fechaInicio = fecha.with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime fechaFin = fecha.with(TemporalAdjusters.lastDayOfMonth());
		BigDecimal porcentajeEsperado = BigDecimal.valueOf(1.0);
		List<Double> cantidadTransaccionesMesual = new ArrayList<>();
		cantidadTransaccionesMesual.add(100D);
		cantidadTransaccionesMesual.add(100D);
		cantidadTransaccionesMesual.add(100D);
		cantidadTransaccionesMesual.add(100D);
		cantidadTransaccionesMesual.add(100D);
		cantidadTransaccionesMesual.add(100D);
		Transaccion transaccion = new TransaccionTestDataBuilder().build();
		RepositorioTransaccion repositorioTransaccionMock = Mockito.mock(RepositorioTransaccion.class);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaOrigen())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaDestino())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(transaccion.getIdCuentaOrigen(),fechaInicio,fechaFin)).thenReturn(cantidadTransaccionesMesual);

		// act
		BigDecimal resultado = transaccion.conocerValorPorcentajeDeTransaccion(cantidadTransaccionesMesual);

		// assert
		Assert.assertEquals(porcentajeEsperado, resultado);
	}

	@Test
	public void seEsperaProcentajeDeDescuentoSegunMinimaCantidadDeTransaccionesEnElMesTest() {
		// arrange
		LocalDateTime fecha = LocalDateTime.now();
		LocalDateTime fechaInicio = fecha.with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime fechaFin = fecha.with(TemporalAdjusters.lastDayOfMonth());
		BigDecimal porcentajeEsperado = BigDecimal.valueOf(0.5);
		List<Double> cantidadTransaccionesMesual = new ArrayList<>();
		cantidadTransaccionesMesual.add(100D);
		cantidadTransaccionesMesual.add(100D);
		Transaccion transaccion = new TransaccionTestDataBuilder().build();
		RepositorioTransaccion repositorioTransaccionMock = Mockito.mock(RepositorioTransaccion.class);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaOrigen())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaDestino())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(transaccion.getIdCuentaOrigen(),fechaInicio,fechaFin)).thenReturn(cantidadTransaccionesMesual);

		// act
		BigDecimal resultado = transaccion.conocerValorPorcentajeDeTransaccion(cantidadTransaccionesMesual);

		// assert
		Assert.assertEquals(porcentajeEsperado, resultado);
	}

	@Test
	public void seEsperaUnFalloComparandoMontoTotalConELTotalPermitidoPorLaCuentaTest() {
		// arrange
		Transaccion transaccion = new TransaccionTestDataBuilder().conFechaValida("2016-03-04 11:30").build();
		LocalDateTime fecha = transaccion.getFechaCreacion();
		LocalDateTime fechaInicio = fecha.with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime fechaFin = fecha.with(TemporalAdjusters.lastDayOfMonth());
		List<Double> cantidadTransaccionesMesual = new ArrayList<>();
		cantidadTransaccionesMesual.add(100D);
		cantidadTransaccionesMesual.add(200D);
		Double montoTransaccionPermitida = 250D;
		RepositorioTransaccion repositorioTransaccionMock = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuentaMock = Mockito.mock(RepositorioCuenta.class);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaOrigen())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaDestino())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(transaccion.getIdCuentaOrigen(),fechaInicio,fechaFin)).thenReturn(cantidadTransaccionesMesual);
		Mockito.when(repositorioTransaccionMock.obtenerElMontoMaximoDeUnCuentaSegunSuId(transaccion.getIdCuentaOrigen())).thenReturn(montoTransaccionPermitida);

		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccionMock,
				repositorioCuentaMock);

		// act - assert
		BasePrueba.assertThrows(() -> servicioCrearTransaccion.ejecutar(transaccion),
				ExcepcionDuplicidad.class, EL_VALOR_DE_LAS_TRANSACCIONES_REALIZADAS_EN_ESTE_MES_SUPERAN_EL_MONTO_MAXIMO_DE_LA_CUENTA);
	}

	@Test
	public void seEsperaUnaTransaccionConNuevoMontoYNuevoPorcentajeTest() {
		// arrange
		LocalDateTime fecha = LocalDateTime.now();
		LocalDateTime fechaInicio = fecha.with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime fechaFin = fecha.with(TemporalAdjusters.lastDayOfMonth());
		Double nuevoPorcentajeEsperado = 0.5;
		Double nuevoMontoEsperado = 300D;
		List<Double> cantidadTransaccionesMesual = new ArrayList<>();
		cantidadTransaccionesMesual.add(100D);
		cantidadTransaccionesMesual.add(200D);
		Double montoTransaccionPermitida = 350D;

		Transaccion transaccion = new TransaccionTestDataBuilder().build();
		RepositorioTransaccion repositorioTransaccionMock = Mockito.mock(RepositorioTransaccion.class);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaOrigen())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaDestino())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(transaccion.getIdCuentaOrigen(), fechaInicio, fechaFin)).thenReturn(cantidadTransaccionesMesual);
		Mockito.when(repositorioTransaccionMock.obtenerElMontoMaximoDeUnCuentaSegunSuId(transaccion.getIdCuentaOrigen())).thenReturn(montoTransaccionPermitida);

		// act
		transaccion.conNuevoMontoYNuevoPorcentajeTransaccion(nuevoMontoEsperado, nuevoPorcentajeEsperado);

		// assert
		Assert.assertEquals(nuevoMontoEsperado, transaccion.getValorTransaccion());
		Assert.assertEquals(nuevoPorcentajeEsperado, transaccion.getPorcentajeDescuento());
	}

	@Test
	public void seEsperaUnFalloEnCalcularSaldosNegativosDuranteCreacionDeTransaccionTest() {
		// arrange
		Transaccion transaccion = new TransaccionTestDataBuilder().conFechaValida("2016-03-04 11:30").conValorTransaccion(10D).build();
		DtoCuenta cuentaOrigen = new CuentaTestDataBuilder().conId(2L).conMonto(1D).buildDto();
		DtoCuenta cuentaDestino = new CuentaTestDataBuilder().conId(3L).conMonto(1D).buildDto();
		List<DtoCuenta> listaCuentaOrigen = new ArrayList<>();
		listaCuentaOrigen.add(cuentaOrigen);
		List<DtoCuenta> listaCuentaDestino = new ArrayList<>();
		listaCuentaDestino.add(cuentaDestino);

		Long idTransaccionCreada = 2L;
		LocalDateTime fecha = transaccion.getFechaCreacion();
		LocalDateTime fechaInicio = fecha.with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime fechaFin = fecha.with(TemporalAdjusters.lastDayOfMonth());
		List<Double> cantidadTransaccionesMesual = new ArrayList<>();
		cantidadTransaccionesMesual.add(100D);
		Double montoTransaccionPermitida = 250D;

		RepositorioTransaccion repositorioTransaccionMock = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuentaMock = Mockito.mock(RepositorioCuenta.class);
		ServicioCrearTransaccion servicioCrearTransaccionMock = Mockito.mock(ServicioCrearTransaccion.class);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaOrigen())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaDestino())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(transaccion.getIdCuentaOrigen(),fechaInicio,fechaFin)).thenReturn(cantidadTransaccionesMesual);
		Mockito.when(repositorioTransaccionMock.obtenerElMontoMaximoDeUnCuentaSegunSuId(transaccion.getIdCuentaOrigen())).thenReturn(montoTransaccionPermitida);
		Mockito.when(servicioCrearTransaccionMock.ejecutar(transaccion)).thenReturn(idTransaccionCreada);
		Mockito.when(repositorioCuentaMock.obtenerCuentaSegunId(transaccion.getIdCuentaDestino())).thenReturn(listaCuentaDestino);
		Mockito.when(repositorioCuentaMock.obtenerCuentaSegunId(transaccion.getIdCuentaOrigen())).thenReturn(listaCuentaOrigen);
		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccionMock,
				repositorioCuentaMock);

		// act - assert
		BasePrueba.assertThrows(() -> servicioCrearTransaccion.ejecutar(transaccion),
				ExcepcionDuplicidad.class, UNO_DE_LOS_VALORES_RESULTO_NEGATIVO_NO_POSIBLE_CONTINUAR_CON_TRANSACCION);
	}

	@Test
	public void seEsperaUnErrorAlActualizarMontosDeLasCuentasTest() {
		// arrange
		Transaccion transaccion = new TransaccionTestDataBuilder().conFechaValida("2016-03-04 11:30").conValorTransaccion(10D).build();
		DtoCuenta cuentaOrigen = new CuentaTestDataBuilder().conId(2L).conMonto(1D).buildDto();
		DtoCuenta cuentaDestino = new CuentaTestDataBuilder().conId(3L).conMonto(1D).buildDto();
		List<DtoCuenta> listaCuentaOrigen = new ArrayList<>();
		listaCuentaOrigen.add(cuentaOrigen);
		List<DtoCuenta> listaCuentaDestino = new ArrayList<>();
		listaCuentaDestino.add(cuentaDestino);

		Long idTransaccionCreada = 2L;
		LocalDateTime fecha = transaccion.getFechaCreacion();
		LocalDateTime fechaInicio = fecha.with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime fechaFin = fecha.with(TemporalAdjusters.lastDayOfMonth());
		List<Double> cantidadTransaccionesMesual = new ArrayList<>();
		cantidadTransaccionesMesual.add(100D);
		Double montoTransaccionPermitida = 250D;

		RepositorioTransaccion repositorioTransaccionMock = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuentaMock = Mockito.mock(RepositorioCuenta.class);
		ServicioCrearTransaccion servicioCrearTransaccionMock = Mockito.mock(ServicioCrearTransaccion.class);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaOrigen())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaDestino())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(transaccion.getIdCuentaOrigen(),fechaInicio,fechaFin)).thenReturn(cantidadTransaccionesMesual);
		Mockito.when(repositorioTransaccionMock.obtenerElMontoMaximoDeUnCuentaSegunSuId(transaccion.getIdCuentaOrigen())).thenReturn(montoTransaccionPermitida);
		Mockito.when(servicioCrearTransaccionMock.ejecutar(transaccion)).thenReturn(idTransaccionCreada);
		Mockito.when(repositorioCuentaMock.obtenerCuentaSegunId(transaccion.getIdCuentaDestino())).thenReturn(null);
		Mockito.when(repositorioCuentaMock.obtenerCuentaSegunId(transaccion.getIdCuentaOrigen())).thenReturn(null);
		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccionMock,
				repositorioCuentaMock);

		// act - assert
		BasePrueba.assertThrows(() -> servicioCrearTransaccion.ejecutar(transaccion),
				NullPointerException.class, OCURRIO_UN_ERROR_DURANTE_EL_PROCESO_DE_ACTUALIZACION);
	}


	@Test
	public void seEsperaQueLaTransaccionSeCreeConExito() {
		// arrange
		Transaccion transaccion = new TransaccionTestDataBuilder().conFechaValida("2016-03-04 11:30").conValorTransaccion(10D).build();
		DtoCuenta cuentaOrigen = new CuentaTestDataBuilder().conId(2L).conMonto(200D).buildDto();
		DtoCuenta cuentaDestino = new CuentaTestDataBuilder().conId(3L).conMonto(100D).buildDto();
		List<DtoCuenta> listaCuentaOrigen = new ArrayList<>();
		listaCuentaOrigen.add(cuentaOrigen);
		List<DtoCuenta> listaCuentaDestino = new ArrayList<>();
		listaCuentaDestino.add(cuentaDestino);

		Long idTransaccionCreada = 2L;
		LocalDateTime fecha = transaccion.getFechaCreacion();
		LocalDateTime fechaInicio = fecha.with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime fechaFin = fecha.with(TemporalAdjusters.lastDayOfMonth());
		List<Double> cantidadTransaccionesMesual = new ArrayList<>();
		cantidadTransaccionesMesual.add(100D);
		Double montoTransaccionPermitida = 250D;
		Double nuevoMontoParaCuentaDestino = 20D;
		Double nuevoMontoParaCuentaOrigen = 10D;

		Cuenta cuentaDestinoFabricada = new Cuenta(cuentaDestino.getId(), cuentaDestino.getNumeroCuenta(),
				cuentaDestino.getMontoMaximo(), nuevoMontoParaCuentaDestino, cuentaDestino.getIdCliente(),
				cuentaDestino.getFechaCreacion());
		Cuenta cuentaOrigenFabricada = new Cuenta(cuentaOrigen.getId(), cuentaOrigen.getNumeroCuenta(),
				cuentaOrigen.getMontoMaximo(), nuevoMontoParaCuentaOrigen, cuentaOrigen.getIdCliente(),
				cuentaOrigen.getFechaCreacion());

		RepositorioTransaccion repositorioTransaccionMock = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuentaMock = Mockito.mock(RepositorioCuenta.class);
		ServicioCrearTransaccion servicioCrearTransaccionMock = Mockito.mock(ServicioCrearTransaccion.class);
		Transaccion transaccionMock = Mockito.mock(Transaccion.class);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaOrigen())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.verificarFechaValidesEnCuenta(transaccion.getIdCuentaDestino())).thenReturn(true);
		Mockito.when(repositorioTransaccionMock.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(transaccion.getIdCuentaOrigen(),fechaInicio,fechaFin)).thenReturn(cantidadTransaccionesMesual);
		Mockito.when(repositorioTransaccionMock.obtenerElMontoMaximoDeUnCuentaSegunSuId(transaccion.getIdCuentaOrigen())).thenReturn(montoTransaccionPermitida);
		Mockito.when(servicioCrearTransaccionMock.ejecutar(transaccion)).thenReturn(idTransaccionCreada);
		Mockito.when(repositorioCuentaMock.obtenerCuentaSegunId(transaccion.getIdCuentaDestino())).thenReturn(listaCuentaDestino);
		Mockito.when(repositorioCuentaMock.obtenerCuentaSegunId(transaccion.getIdCuentaOrigen())).thenReturn(listaCuentaOrigen);
		Mockito.doNothing().when(transaccionMock).verificarSaldosNegativos(nuevoMontoParaCuentaDestino,nuevoMontoParaCuentaOrigen);

		Mockito.doNothing().when(repositorioCuentaMock).actualizar(cuentaDestinoFabricada);
		Mockito.doNothing().when(repositorioCuentaMock).actualizar(cuentaOrigenFabricada);

		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccionMock,
				repositorioCuentaMock);

		// act
		servicioCrearTransaccion.ejecutar(transaccion);

		//assert
		Assert.assertEquals(cuentaDestinoFabricada.getMonto(), nuevoMontoParaCuentaDestino);
	}
}
