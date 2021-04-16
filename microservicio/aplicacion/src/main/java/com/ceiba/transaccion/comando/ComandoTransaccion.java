package com.ceiba.transaccion.comando;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComandoTransaccion{
	private Long id;
	private Long idCuentaOrigen;
	private Long idCuentaDestino;
    private Double valorTransaccion;
    private Double porcentajeDescuento;
    private LocalDateTime fechaCreacion;
    private Short estado;
}
