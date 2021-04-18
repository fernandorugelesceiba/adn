package com.ceiba.cuenta.servicio;

import com.ceiba.BasePrueba;
import com.ceiba.cuenta.modelo.entidad.Cuenta;
import com.ceiba.cuenta.puerto.repositorio.RepositorioCuenta;
import com.ceiba.cuenta.servicio.ServicioActualizarCuenta;
import com.ceiba.cuenta.servicio.ServicioCrearCuenta;
import com.ceiba.cuenta.servicio.testdatabuilder.CuentaTestDataBuilder;
import com.ceiba.dominio.excepcion.ExcepcionDuplicidad;
import com.ceiba.usuario.modelo.entidad.Usuario;
import com.ceiba.usuario.puerto.repositorio.RepositorioUsuario;
import com.ceiba.usuario.servicio.ServicioActualizarUsuario;
import com.ceiba.usuario.servicio.testdatabuilder.UsuarioTestDataBuilder;
import org.junit.Test;
import org.mockito.Mockito;

public class ServicioActualizarCuentaTest {
    private static final String LA_CUENTA_YA_EXISTE_EN_EL_SISTEMA = "la cuenta ya existe en el sistema";

    @Test
    public void validarUsuarioExistenciaPreviaTest() {
        // arrange
        Cuenta cuenta = new CuentaTestDataBuilder().build();
        RepositorioCuenta repositorioCuenta = Mockito.mock(RepositorioCuenta.class);
        Mockito.when(repositorioCuenta.existe(Mockito.anyString())).thenReturn(true);
        ServicioActualizarCuenta servicioActualizarCuenta = new ServicioActualizarCuenta(repositorioCuenta);
        // act - assert
        BasePrueba.assertThrows(() -> servicioActualizarCuenta.ejecutar(cuenta), ExcepcionDuplicidad.class,LA_CUENTA_YA_EXISTE_EN_EL_SISTEMA);
    }
}
