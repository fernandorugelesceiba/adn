package com.ceiba.cuenta.servicio;


import org.junit.Test;
import org.mockito.Mockito;

import com.ceiba.BasePrueba;
import com.ceiba.cuenta.modelo.entidad.Cuenta;
import com.ceiba.cuenta.puerto.repositorio.RepositorioCuenta;
import com.ceiba.cuenta.servicio.testdatabuilder.CuentaTestDataBuilder;
import com.ceiba.dominio.excepcion.ExcepcionDuplicidad;


public class ServicioCrearCuentaTest {
	private static final String LA_CUENTA_YA_EXISTE_EN_EL_SISTEMA = "la cuenta ya existe en el sistema";

	@Test
	public void validarCuentaExistenciaPreviaTest() {

		// arrange
		Cuenta cuenta = new CuentaTestDataBuilder().build();
		RepositorioCuenta repositorioCuenta = Mockito.mock(RepositorioCuenta.class);
		Mockito.when(repositorioCuenta.existe(Mockito.anyString())).thenReturn(true);
		ServicioCrearCuenta servicioCrearCuenta = new ServicioCrearCuenta(repositorioCuenta);
		// act - assert
		BasePrueba.assertThrows(() -> servicioCrearCuenta.ejecutar(cuenta), ExcepcionDuplicidad.class,LA_CUENTA_YA_EXISTE_EN_EL_SISTEMA);
	}
}
