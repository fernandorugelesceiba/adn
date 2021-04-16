package com.ceiba.cuenta.controlador;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceiba.cuenta.consulta.ManejadorListarCuentas;
import com.ceiba.cuenta.consulta.ManejadorListarCuentasPorCliente;
import com.ceiba.cuenta.modelo.dto.DtoCuenta;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cuentas")
@Api(tags = { "Controlador consulta cuentas" })
public class ConsultaControladorCuenta {

	private final ManejadorListarCuentas manejadorListarCuentas;
	private final ManejadorListarCuentasPorCliente manejadorListarCuentasPorCliente;

	public ConsultaControladorCuenta(ManejadorListarCuentas manejadorListarCuentas, ManejadorListarCuentasPorCliente manejadorListarCuentasPorCliente) {
		this.manejadorListarCuentas = manejadorListarCuentas;
		this.manejadorListarCuentasPorCliente = manejadorListarCuentasPorCliente;
	}

	@GetMapping()
	@ApiOperation("Listar cuentas")
	public List<DtoCuenta> listar() {
		return this.manejadorListarCuentas.ejecutar();
	}
	
	@GetMapping("id")
	@ApiOperation("Listar cuentas segun el cliente")
	public List<DtoCuenta> listarCuentasPorCliente(@RequestParam("idCuenta") Long idCuenta) {
		return this.manejadorListarCuentasPorCliente.ejecutar(idCuenta);
	}

}
