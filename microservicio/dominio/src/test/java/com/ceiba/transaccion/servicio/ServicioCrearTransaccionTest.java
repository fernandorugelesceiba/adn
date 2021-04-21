package com.ceiba.transaccion.servicio;

import com.ceiba.BasePrueba;
import com.ceiba.cuenta.puerto.repositorio.RepositorioCuenta;
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
	private static final String LA_TRANSACCION_NO_SE_REALIZA_POR_CUENTA_RECIEN_CREADA = "Lo sentimos por el momento no se pueden realizar transacciones, las cuentas deben llevar mas de un dia de creadas ya que por temas de validacion la entidad lo exige";
	private static final String EL_VALOR_DE_LAS_TRANSACCIONES_REALIZADAS_EN_ESTE_MES_SUPERAN_EL_MONTO_MAXIMO_DE_LA_CUENTA = "El monto total de transacciones supera el monto maximo permito";
	private static final String SUS_TRANSACCIONES_SUPERAN_EL_MONTO_MAXIMO_ESTABLECIDO_POR_LA_ENTIDAD = "El total de sus transacciones en este mes, superan el monto permitido impuesto por la entidad";

	@Test
	public void seEsperaUnFalloVerificandoQueLaCuentaLleveUnDiaDeCreadaTest() {
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

}
