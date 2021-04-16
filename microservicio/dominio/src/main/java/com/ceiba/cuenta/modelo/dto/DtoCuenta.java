package com.ceiba.cuenta.modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DtoCuenta {
	private Long id;
    private String numeroCuenta;
    private Double montoMaximo;
    private Double monto;
    private Long idCliente;
    private LocalDateTime fechaCreacion;
}
