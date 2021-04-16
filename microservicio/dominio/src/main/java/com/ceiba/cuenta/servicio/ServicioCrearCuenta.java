package com.ceiba.cuenta.servicio;

import com.ceiba.cuenta.modelo.entidad.Cuenta;
import com.ceiba.cuenta.puerto.repositorio.RepositorioCuenta;
import com.ceiba.dominio.excepcion.ExcepcionDuplicidad;


public class ServicioCrearCuenta {

    private static final String LA_CUENTA_YA_EXISTE_EN_EL_SISTEMA = "la cuenta ya existe en el sistema";

    private final RepositorioCuenta repositorioCuenta;

    public ServicioCrearCuenta(RepositorioCuenta repositorioCuenta) {
        this.repositorioCuenta = repositorioCuenta;
    }

    public Long ejecutar(Cuenta cuenta) {
        validarExistenciaPrevia(cuenta);
        return this.repositorioCuenta.crear(cuenta);
    }

    private void validarExistenciaPrevia(Cuenta cuenta) {
        boolean existe = this.repositorioCuenta.existe(cuenta.getNumeroCuenta());
        if(existe) {
            throw new ExcepcionDuplicidad(LA_CUENTA_YA_EXISTE_EN_EL_SISTEMA);
        }
    }
}
