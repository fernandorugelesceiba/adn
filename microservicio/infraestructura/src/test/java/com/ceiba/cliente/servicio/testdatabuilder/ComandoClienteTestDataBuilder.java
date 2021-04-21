package com.ceiba.cliente.servicio.testdatabuilder;

import com.ceiba.cliente.comando.ComandoCliente;

import java.time.LocalDateTime;
import java.util.UUID;

public class ComandoClienteTestDataBuilder {

    private Long id;
    private String nombre;
    private String apellido;
    private Short tipoDocumento;
    private String numeroDocumento;
    private String Clave;
    private LocalDateTime fechaCreacion;
    private Long idUsuarioCreacion;

    public ComandoClienteTestDataBuilder() {
        id = 1L;
        nombre = UUID.randomUUID().toString();
        apellido = UUID.randomUUID().toString();
        tipoDocumento = 1;
        numeroDocumento = UUID.randomUUID().toString();
        Clave = UUID.randomUUID().toString();
        fechaCreacion = LocalDateTime.now();
        idUsuarioCreacion = 1L;
    }

    public ComandoCliente build() {
        return new ComandoCliente(id, nombre, apellido, tipoDocumento, numeroDocumento, Clave, fechaCreacion, idUsuarioCreacion);
    }

    public ComandoClienteTestDataBuilder conTipoDocumento(Short tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
        return this;
    }

    public ComandoClienteTestDataBuilder conNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
        return this;
    }
}
