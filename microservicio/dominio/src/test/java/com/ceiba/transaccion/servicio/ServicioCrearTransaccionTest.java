package com.ceiba.transaccion.servicio;

import com.ceiba.cuenta.modelo.dto.DtoCuenta;
import com.ceiba.cuenta.modelo.entidad.Cuenta;
import com.ceiba.cuenta.servicio.testdatabuilder.CuentaTestDataBuilder;
import com.ceiba.transaccion.modelo.dto.DtoTransaccion;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.ceiba.BasePrueba;
import com.ceiba.cuenta.puerto.repositorio.RepositorioCuenta;
import com.ceiba.dominio.excepcion.ExcepcionDuplicidad;
import com.ceiba.transaccion.modelo.entidad.Transaccion;
import com.ceiba.transaccion.puerto.repositorio.RepositorioTransaccion;
import com.ceiba.transaccion.servicio.testdatabuilder.TransaccionTestDataBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ServicioCrearTransaccionTest {
	private static final String LA_TRANSACCION_NO_SE_REALIZA_POR_CUENTA_RECIEN_CREADA = "Lo sentimos por el momento no se pueden realizar transacciones, las cuentas deben llevar mas de un dia de creadas ya que por temas de validacion la entidad lo exige";
	private static final String EL_VALOR_DE_LAS_TRANSACCIONES_REALIZADAS_EN_ESTE_MES_SUPERAN_EL_MONTO_MAXIMO_DE_LA_CUENTA = "El monto total de transacciones supera el monto maximo permito";
	private static final String OCURRIO_UN_ERROR_DURANTE_EL_PROCESO_DE_ACTUALIZACION = "Ocurrio un error durante el proceso de actualiación";
	private static final String UNO_DE_LOS_VALORES_RESULTO_NEGATIVO_NO_POSIBLE_CONTINUAR_CON_TRANSACCION = "Uno de los saldos resulto negativo, no se puede continuar con el proceso";

	@Test
	public void verificarQueLaCuentaLleveUnDiaDeCreadaTest() {

		// arrange
		Transaccion transaccion = new TransaccionTestDataBuilder().build();
		RepositorioTransaccion repositorioTransaccion = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuenta = Mockito.mock(RepositorioCuenta.class);
		Mockito.when(repositorioTransaccion.verificarFechaValidesEnCuenta(Mockito.anyLong())).thenReturn(false);
		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccion,
				repositorioCuenta);

		// act - assert
		BasePrueba.assertThrows(() -> servicioCrearTransaccion.ejecutar(transaccion),
				ExcepcionDuplicidad.class, LA_TRANSACCION_NO_SE_REALIZA_POR_CUENTA_RECIEN_CREADA);
	}


	@Test
	public void crearValorPorcentajeDeTransaccionTest() {
		// arrange
		Double valorTransaccionResultado = 99.5D;
		Double valorTotalDeTransacciones = 100D;
		List<Double> lista = new ArrayList<>();
		Transaccion transaccion = new TransaccionTestDataBuilder().build();
		ServicioCrearTransaccion servicioCrearTransaccionMock = Mockito.mock(ServicioCrearTransaccion.class);
		Transaccion transaccionMock = Mockito.mock(Transaccion.class);
		RepositorioTransaccion repositorioTransaccion = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuenta = Mockito.mock(RepositorioCuenta.class);
		Mockito.when(transaccionMock.getIdCuentaOrigen()).thenReturn(transaccion.getIdCuentaOrigen());
		Mockito.when(transaccionMock.getValorTransaccion()).thenReturn(transaccion.getValorTransaccion());
		Mockito.when(servicioCrearTransaccionMock.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(
				transaccion.getIdCuentaOrigen())).thenReturn(lista);

		Mockito.doNothing().when(servicioCrearTransaccionMock).verificarMontoTotalDeLasTransaccionesSegunELMontoMaximoDeLaCuenta(
				transaccion.getIdCuentaOrigen(), valorTotalDeTransacciones);
		Mockito.when(repositorioTransaccion.verificarFechaValidesEnCuenta(Mockito.anyLong())).thenReturn(false);
		Mockito.when(repositorioTransaccion.obtenerElMontoMaximoDeUnCuentaSegunSuId(transaccion.getIdCuentaOrigen())).thenReturn(500D);
		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccion,
				repositorioCuenta);

		// act
		Transaccion resultado = servicioCrearTransaccion.crearValorPorcentajeDeTransaccion(transaccion);

		// assert
		Assert.assertEquals(valorTransaccionResultado, resultado.getValorTransaccion());
	}

	@Test
	public void obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotalTest() {
		// arrange
		List<Double> listaEsperada = new ArrayList<>();
		listaEsperada.add(0.5);
		listaEsperada.add(300D);
		listaEsperada.add(100D);

		Transaccion transaccion = new TransaccionTestDataBuilder().build();
		RepositorioTransaccion repositorioTransaccion = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuenta = Mockito.mock(RepositorioCuenta.class);
		Mockito.when(repositorioTransaccion.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(transaccion.getIdCuentaOrigen())).thenReturn(listaEsperada);
		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccion,
				repositorioCuenta);

		// act
		List<Double> resultado = servicioCrearTransaccion.obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(transaccion.getIdCuentaOrigen());

		// assert
		Assert.assertEquals(listaEsperada, resultado);
	}

	@Test
	public void verificarMontoTotalDeLasTransaccionesSegunELMontoMaximoDeLaCuentaLanzandoExceptionTest() {
		// arrange
		Double montoTotalTransaccion = 500D;
		Double montoMaximoPermitido = 400D;
		Transaccion transaccion = new TransaccionTestDataBuilder().build();
		RepositorioTransaccion repositorioTransaccion = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuenta = Mockito.mock(RepositorioCuenta.class);
		Mockito.when(repositorioTransaccion.obtenerElMontoMaximoDeUnCuentaSegunSuId(
				transaccion.getIdCuentaOrigen())).thenReturn(montoMaximoPermitido);
		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccion,
				repositorioCuenta);


		// act - assert
		BasePrueba.assertThrows(() -> servicioCrearTransaccion.verificarMontoTotalDeLasTransaccionesSegunELMontoMaximoDeLaCuenta(transaccion.getIdCuentaOrigen(), montoTotalTransaccion),
				ExcepcionDuplicidad.class, EL_VALOR_DE_LAS_TRANSACCIONES_REALIZADAS_EN_ESTE_MES_SUPERAN_EL_MONTO_MAXIMO_DE_LA_CUENTA);

	}


	@Test
	public void ejecutarActualizacionDeMontosEnCuentasRecibiendoUnValorNullTest() {
		// arrange
		Double valorProcentajeDeTransaccion = 0.5;
		List<DtoCuenta> listaDtoCuenta = new ArrayList<>();
		Transaccion transaccion = new TransaccionTestDataBuilder().build();
		Transaccion transaccionConValorProcentajeDeTransaccion = new TransaccionTestDataBuilder().build();

		Transaccion transaccionMock = Mockito.mock(Transaccion.class);
		DtoCuenta dtoCuenta = new CuentaTestDataBuilder().buildDto();
		Cuenta cuenta = new CuentaTestDataBuilder().build();
		listaDtoCuenta.add(dtoCuenta);

		RepositorioTransaccion repositorioTransaccion = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuenta = Mockito.mock(RepositorioCuenta.class);
		Mockito.when(transaccionMock.getPorcentajeDescuento()).thenReturn(valorProcentajeDeTransaccion);

		Mockito.when(repositorioCuenta.obtenerCuentaSegunId(transaccion.getIdCuentaDestino())).thenReturn(listaDtoCuenta);
		Mockito.when(repositorioCuenta.obtenerCuentaSegunId(transaccion.getIdCuentaOrigen())).thenReturn(listaDtoCuenta);

		Mockito.doNothing().when(repositorioCuenta).actualizar(cuenta);
		Mockito.doNothing().when(repositorioCuenta).actualizar(cuenta);

		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccion,
				repositorioCuenta);

		// act - assert
		BasePrueba.assertThrows(() -> servicioCrearTransaccion.ejecutarActualizacionDeMontosEnCuentas(null,transaccionConValorProcentajeDeTransaccion),
				NullPointerException.class, OCURRIO_UN_ERROR_DURANTE_EL_PROCESO_DE_ACTUALIZACION);
	}

	@Test
	public void verficarQueLaCuentaLleveUnDiaDeCreadaTest() {
		// arrange
		Transaccion transaccion = new TransaccionTestDataBuilder().build();
		RepositorioTransaccion repositorioTransaccion = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuenta = Mockito.mock(RepositorioCuenta.class);
		Mockito.when(repositorioTransaccion.verificarFechaValidesEnCuenta(transaccion.getIdCuentaDestino())).thenReturn(false);
		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccion,
				repositorioCuenta);

		// act - assert
		BasePrueba.assertThrows(() -> servicioCrearTransaccion.verficarQueLaCuentaLleveUnDiaDeCreada(transaccion.getIdCuentaOrigen()),
				ExcepcionDuplicidad.class, LA_TRANSACCION_NO_SE_REALIZA_POR_CUENTA_RECIEN_CREADA);
	}

	@Test
	public void verificarSaldosNegativosTest() {
		// arrange
		Double nuevoMontoParaCuentaDestino = -100D;
		Double nuevoMontoParaCuentaOrigen = -100D;
		RepositorioTransaccion repositorioTransaccion = Mockito.mock(RepositorioTransaccion.class);
		RepositorioCuenta repositorioCuenta = Mockito.mock(RepositorioCuenta.class);
		ServicioCrearTransaccion servicioCrearTransaccion = new ServicioCrearTransaccion(repositorioTransaccion,
				repositorioCuenta);

		// act - assert
		BasePrueba.assertThrows(() -> servicioCrearTransaccion.verificarSaldosNegativos(nuevoMontoParaCuentaDestino, nuevoMontoParaCuentaOrigen),
				ExcepcionDuplicidad.class, UNO_DE_LOS_VALORES_RESULTO_NEGATIVO_NO_POSIBLE_CONTINUAR_CON_TRANSACCION);
	}

}
