package com.ceiba.cuenta.comando.manejador;

import org.springframework.stereotype.Component;

import com.ceiba.cuenta.comando.ComandoCuenta;
import com.ceiba.cuenta.comando.fabrica.FabricaCuenta;
import com.ceiba.cuenta.modelo.entidad.Cuenta;
import com.ceiba.cuenta.servicio.ServicioActualizarCuenta;
import com.ceiba.manejador.ManejadorComando;

@Component
public class ManejadorActualizarCuenta implements ManejadorComando<ComandoCuenta> {

    private final FabricaCuenta fabricaCuenta;
    private final ServicioActualizarCuenta servicioActualizarCuenta;

    public ManejadorActualizarCuenta(FabricaCuenta fabricaCuenta, ServicioActualizarCuenta servicioActualizarCuenta) {
        this.fabricaCuenta = fabricaCuenta;
        this.servicioActualizarCuenta = servicioActualizarCuenta;
    }

    public void ejecutar(ComandoCuenta comandoCuenta) {
        Cuenta cuenta = this.fabricaCuenta.crear(comandoCuenta);
        this.servicioActualizarCuenta.ejecutar(cuenta);
    }
}
