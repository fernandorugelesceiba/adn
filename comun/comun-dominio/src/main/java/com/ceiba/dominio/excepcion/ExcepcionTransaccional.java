package com.ceiba.dominio.excepcion;

public class ExcepcionTransaccional extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExcepcionTransaccional(String mensaje) {
        super(mensaje);
    }
}
