package com.ceiba.cuenta.comando.manejador;

import org.springframework.stereotype.Component;

import com.ceiba.cuenta.servicio.ServicioEliminarCuenta;
import com.ceiba.manejador.ManejadorComando;


@Component
public class ManejadorEliminarCuenta implements ManejadorComando<Long> {

    private final ServicioEliminarCuenta servicioEliminarCuenta;

    public ManejadorEliminarCuenta(ServicioEliminarCuenta servicioEliminarCuenta) {
        this.servicioEliminarCuenta = servicioEliminarCuenta;
    }

    public void ejecutar(Long idCuenta) {
        this.servicioEliminarCuenta.ejecutar(idCuenta);
    }
}
