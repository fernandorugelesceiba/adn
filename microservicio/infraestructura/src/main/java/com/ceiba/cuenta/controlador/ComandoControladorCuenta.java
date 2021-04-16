package com.ceiba.cuenta.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceiba.ComandoRespuesta;
import com.ceiba.cuenta.comando.ComandoCuenta;
import com.ceiba.cuenta.comando.manejador.ManejadorActualizarCuenta;
import com.ceiba.cuenta.comando.manejador.ManejadorCrearCuenta;
import com.ceiba.cuenta.comando.manejador.ManejadorEliminarCuenta;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cuentas")
@Api(tags = { "Controlador comando cuenta" })
public class ComandoControladorCuenta {

	private final ManejadorCrearCuenta manejadorCrearCuenta;
	private final ManejadorEliminarCuenta manejadorEliminarCuenta;
	private final ManejadorActualizarCuenta manejadorActualizarCuenta;

	@Autowired
	public ComandoControladorCuenta(ManejadorCrearCuenta manejadorCrearCuenta,
			ManejadorEliminarCuenta manejadorEliminarCuenta, ManejadorActualizarCuenta manejadorActualizarCuenta) {
		this.manejadorCrearCuenta = manejadorCrearCuenta;
		this.manejadorEliminarCuenta = manejadorEliminarCuenta;
		this.manejadorActualizarCuenta = manejadorActualizarCuenta;
	}

	@PostMapping
	@ApiOperation("Crear Cuenta")
	public ComandoRespuesta<Long> crear(@RequestBody ComandoCuenta comandoCuenta) {
		return manejadorCrearCuenta.ejecutar(comandoCuenta);
	}

	@DeleteMapping(value = "/{id}")
	@ApiOperation("Eliminar Cuenta")
	public void eliminar(@PathVariable Long id) {
		manejadorEliminarCuenta.ejecutar(id);
	}

	@PutMapping(value = "/{id}")
	@ApiOperation("Actualizar Cuenta")
	public void actualizar(@RequestBody ComandoCuenta comandoCuenta, @PathVariable Long id) {
		comandoCuenta.setId(id);
		manejadorActualizarCuenta.ejecutar(comandoCuenta);
	}
}
