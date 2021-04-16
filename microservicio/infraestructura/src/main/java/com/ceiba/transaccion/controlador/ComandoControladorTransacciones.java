package com.ceiba.transaccion.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceiba.ComandoRespuesta;
import com.ceiba.transaccion.comando.ComandoTransaccion;
import com.ceiba.transaccion.comando.manejador.ManejadorCrearTransaccion;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/transacciones")
@Api(tags = { "Controlador comando transaccion" })
public class ComandoControladorTransacciones {

	private final ManejadorCrearTransaccion manejadorCrearTransaccion;

	@Autowired
	public ComandoControladorTransacciones(ManejadorCrearTransaccion manejadorCrearTransaccion) {
		this.manejadorCrearTransaccion = manejadorCrearTransaccion;
	}

	@PostMapping
	@ApiOperation("Crear Transaccion")
	public ComandoRespuesta<Long> crear(@RequestBody ComandoTransaccion comandoTransaccion) {
		return manejadorCrearTransaccion.ejecutar(comandoTransaccion);
	}

}
