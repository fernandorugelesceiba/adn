package com.ceiba.cliente.comando;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComandoCliente{

    private Long id;
    private String nombre;
    private String apellido;
    private Short tipoDocumento;
    private String numeroDocumento;
    private String Clave;
    private LocalDateTime fechaCreacion;
    private Long idUsuarioCreacion;
}
