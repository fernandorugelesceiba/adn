package com.ceiba.transaccion.testdatabuilder;

import com.ceiba.transaccion.comando.ComandoTransaccion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ComandoTransaccionTestDataBuilder {
    private static final String FORMATO_FECHA =  "yyyy-MM-dd HH:mm";

    private Long id;
    private Long idCuentaOrigen;
    private Long idCuentaDestino;
    private Double valorTransaccion;
    private Double porcentajeDescuento;
    private LocalDateTime fechaCreacion;
    private Short estado;

    public ComandoTransaccionTestDataBuilder() {
        id = 1L;
        idCuentaOrigen = 1L;
        idCuentaDestino = 2L;
        valorTransaccion = 200.0;
        porcentajeDescuento = 0.5;
        fechaCreacion = LocalDateTime.now();
        estado = 1;
    }

    public ComandoTransaccion build() {
        return new ComandoTransaccion(id, idCuentaOrigen, idCuentaDestino, valorTransaccion, porcentajeDescuento, fechaCreacion, estado);
    }

    public ComandoTransaccionTestDataBuilder conFechaValida(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATO_FECHA);
        this.fechaCreacion = LocalDateTime.parse(fecha, formatter);
        return this;
    }
}
