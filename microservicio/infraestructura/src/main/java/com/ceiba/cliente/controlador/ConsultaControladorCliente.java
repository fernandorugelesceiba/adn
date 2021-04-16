package com.ceiba.cliente.controlador;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceiba.cliente.consulta.ManejadorClientePorDocumentoYTipoDocumento;
import com.ceiba.cliente.consulta.ManejadorListarClientes;
import com.ceiba.cliente.modelo.dto.DtoCliente;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/clientes")
@Api(tags={"Controlador consulta cliente"})
public class ConsultaControladorCliente {

    private final ManejadorListarClientes manejadorListarClientes;
    private final ManejadorClientePorDocumentoYTipoDocumento manejadorClientePorDocumentoYTipoDocumento;

    public ConsultaControladorCliente(ManejadorListarClientes manejadorListarClientes, ManejadorClientePorDocumentoYTipoDocumento manejadorClientePorDocumentoYTipoDocumento) {
        this.manejadorListarClientes = manejadorListarClientes;
        this.manejadorClientePorDocumentoYTipoDocumento = manejadorClientePorDocumentoYTipoDocumento;
    }

    @GetMapping
    @ApiOperation("Listar clientes")
    public List<DtoCliente> listar() {
        return this.manejadorListarClientes.ejecutar();
    }
    
    @GetMapping("/id")
    @ApiOperation("Verificar las credencias del cliente")
    public List<DtoCliente> verificarCredencialesDelCliente(@RequestParam("tipoDocumento") Short tipoDocumento, @RequestParam("numeroDocumento") String numeroDocumento) {
        return this.manejadorClientePorDocumentoYTipoDocumento.ejecutar(tipoDocumento, numeroDocumento);
    }

}
