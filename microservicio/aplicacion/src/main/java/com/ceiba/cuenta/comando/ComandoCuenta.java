package com.ceiba.cuenta.comando;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComandoCuenta{
	private Long id;
    private String numeroCuenta;
    private Double montoMaximo;
    private Double monto;
    private Long idCliente;
    private LocalDateTime fechaCreacion;
}
