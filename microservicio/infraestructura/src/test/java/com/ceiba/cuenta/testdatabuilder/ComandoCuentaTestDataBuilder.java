package com.ceiba.cuenta.testdatabuilder;

import com.ceiba.cuenta.comando.ComandoCuenta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ComandoCuentaTestDataBuilder {
    private static final String FORMATO_FECHA =  "yyyy-MM-dd HH:mm";

    private Long id;
    private String numeroCuenta;
    private Double montoMaximo;
    private Double monto;
    private Long idCliente;
    private LocalDateTime fechaCreacion;

    public ComandoCuentaTestDataBuilder() {
        id = 1L;
        numeroCuenta = UUID.randomUUID().toString();
        montoMaximo = 6500.0;
        monto = 1200.0;
        idCliente = 1L;
        fechaCreacion = LocalDateTime.now();
    }

    public ComandoCuenta build() {
        return new ComandoCuenta(id, numeroCuenta, montoMaximo, monto, idCliente, fechaCreacion);
    }

    public ComandoCuentaTestDataBuilder conFechaValida(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATO_FECHA);
        this.fechaCreacion = LocalDateTime.parse(fecha, formatter);
        return this;
    }
}
