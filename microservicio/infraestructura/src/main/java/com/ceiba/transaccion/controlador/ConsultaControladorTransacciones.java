package com.ceiba.transaccion.controlador;

import com.ceiba.transaccion.consulta.ManejadorListarTransaccion;
import com.ceiba.transaccion.modelo.dto.DtoTransaccion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transacciones")
@Api(tags = { "Controlador consulta transaccion" })
public class ConsultaControladorTransacciones {

	private final ManejadorListarTransaccion manejadorListarTransaccion;

	public ConsultaControladorTransacciones(ManejadorListarTransaccion manejadorListarTransaccion) {
		this.manejadorListarTransaccion = manejadorListarTransaccion;
	}

	@GetMapping("transaccion")
	@ApiOperation("Listar Transaccion")
	public List<DtoTransaccion> listar(@RequestParam("idCuenta") Long idCuenta) {
		return this.manejadorListarTransaccion.ejecutar(idCuenta);
	}

}
