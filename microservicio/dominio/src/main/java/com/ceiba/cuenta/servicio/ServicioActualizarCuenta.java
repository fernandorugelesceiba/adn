package com.ceiba.cuenta.servicio;

import com.ceiba.cuenta.modelo.entidad.Cuenta;
import com.ceiba.cuenta.puerto.repositorio.RepositorioCuenta;

public class ServicioActualizarCuenta {

    private final RepositorioCuenta repositorioCuenta;

    public ServicioActualizarCuenta(RepositorioCuenta repositorioCuenta) {
        this.repositorioCuenta = repositorioCuenta;
    }

    public void ejecutar(Cuenta cuenta) {
        this.repositorioCuenta.actualizar(cuenta);
    }

}
