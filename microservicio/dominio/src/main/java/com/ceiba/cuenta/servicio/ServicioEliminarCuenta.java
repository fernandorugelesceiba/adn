package com.ceiba.cuenta.servicio;

import com.ceiba.cuenta.puerto.repositorio.RepositorioCuenta;

public class ServicioEliminarCuenta {

    private final RepositorioCuenta repositorioCuenta;

    public ServicioEliminarCuenta(RepositorioCuenta repositorioCuenta) {
        this.repositorioCuenta = repositorioCuenta;
    }

    public void ejecutar(Long id) {
        this.repositorioCuenta.eliminar(id);
    }
}
