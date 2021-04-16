package com.ceiba.cuenta.comando.manejador;

import org.springframework.stereotype.Component;

import com.ceiba.ComandoRespuesta;
import com.ceiba.cuenta.comando.ComandoCuenta;
import com.ceiba.cuenta.comando.fabrica.FabricaCuenta;
import com.ceiba.cuenta.modelo.entidad.Cuenta;
import com.ceiba.cuenta.servicio.ServicioCrearCuenta;
import com.ceiba.manejador.ManejadorComandoRespuesta;

@Component
public class ManejadorCrearCuenta implements ManejadorComandoRespuesta<ComandoCuenta, ComandoRespuesta<Long>> {

	private final FabricaCuenta fabricaCuenta;
	private final ServicioCrearCuenta servicioCrearCuenta;

	public ManejadorCrearCuenta(FabricaCuenta fabricaCuenta, ServicioCrearCuenta servicioCrearCuenta) {
		this.fabricaCuenta = fabricaCuenta;
		this.servicioCrearCuenta = servicioCrearCuenta;
	}

	public ComandoRespuesta<Long> ejecutar(ComandoCuenta comandoCuenta) {
		Cuenta cuenta = this.fabricaCuenta.crear(comandoCuenta);
		return new ComandoRespuesta<>(this.servicioCrearCuenta.ejecutar(cuenta));
	}
}
