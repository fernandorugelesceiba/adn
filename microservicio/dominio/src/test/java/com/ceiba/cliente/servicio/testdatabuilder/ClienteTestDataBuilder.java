package com.ceiba.usuario.servicio.testdatabuilder;

import com.ceiba.cliente.modelo.entidad.Cliente;
import com.ceiba.usuario.modelo.entidad.Usuario;

import java.time.LocalDateTime;

public class ClienteTestDataBuilder {

    private Long id;
    private String nombre;
    private String apellido;
    private Short tipoDocumento;
    private String numeroDocumento;
    private String clave;
    private LocalDateTime fechaCreacion;
    private Long idUsuarioCreacion;

    public ClienteTestDataBuilder() {
        id = 1L;
        nombre = "fernando";
        apellido = "rugeles";
        tipoDocumento = 1;
        numeroDocumento = "1098765179";
        clave = "1234";
        fechaCreacion = LocalDateTime.now();
        idUsuarioCreacion = 1L;
    }


    public Cliente build() {
        return new Cliente(id,
                nombre,
                apellido,
                tipoDocumento,
                numeroDocumento,
                clave,
                fechaCreacion,
                idUsuarioCreacion
        );
    }
}
