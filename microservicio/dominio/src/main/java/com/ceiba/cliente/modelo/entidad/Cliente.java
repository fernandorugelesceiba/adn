package com.ceiba.cliente.modelo.entidad;


import lombok.Getter;

import java.time.LocalDateTime;

import static com.ceiba.dominio.ValidadorArgumento.validarLongitud;
import static com.ceiba.dominio.ValidadorArgumento.validarObligatorio;

@Getter
public class Cliente {

    private static final String SE_DEBE_INGRESAR_LA_FECHA_CREACION = "Se debe ingresar la fecha de creaciÃ³n";
    private static final String LA_CLAVE_DEBE_TENER_UNA_LONGITUD_MAYOR_O_IGUAL_A = "La clave debe tener una longitud mayor o igual a %s";
    private static final String SE_DEBE_INGRESAR_LA_CLAVE = "Se debe ingresar la clave";
    private static final String SE_DEBE_INGRESAR_EL_NOMBRE_DEL_CLIENTE = "Se debe ingresar el nombre del cliente";
    private static final String SE_DEBE_INGRESAR_UN_TIPO_DE_DOCUMENTO = "Se debe ingresar el tipo de documento";
    private static final String SE_DEBE_INGRESAR_UN_NUMERO_DE_DOCUMENTO = "Se debe ingresar el numero de documento";
    private static final String SE_DEBE_INGRESAR_EL_APELLIDO_DEL_CLIENTE = "Se debe ingresar el apellido de cliente";
    private static final String SE_DEBE_INGRESAR_USUARIO_DE_CREACION = "Se debe ingresar un usuario de creación";
    private static final int LONGITUD_MINIMA_CLAVE = 4;

    private Long id;
    private String nombre;
    private String apellido;
    private Short tipoDocumento;
    private String numeroDocumento;
    private String clave;
    private LocalDateTime fechaCreacion;
    private Long idUsuarioCreacion;

    public Cliente(Long id,String nombre, String apellido, Short tipoDocumento, String numeroDocumento, String clave, LocalDateTime fechaCreacion, Long idUsuarioCreacion) {
        validarObligatorio(nombre, SE_DEBE_INGRESAR_EL_NOMBRE_DEL_CLIENTE);
        validarObligatorio(apellido, SE_DEBE_INGRESAR_EL_APELLIDO_DEL_CLIENTE);
        validarObligatorio(clave, SE_DEBE_INGRESAR_LA_CLAVE);
        validarLongitud(clave, LONGITUD_MINIMA_CLAVE, String.format(LA_CLAVE_DEBE_TENER_UNA_LONGITUD_MAYOR_O_IGUAL_A,LONGITUD_MINIMA_CLAVE));
        validarObligatorio(fechaCreacion, SE_DEBE_INGRESAR_LA_FECHA_CREACION);
        validarObligatorio(tipoDocumento, SE_DEBE_INGRESAR_UN_TIPO_DE_DOCUMENTO);
        validarObligatorio(numeroDocumento, SE_DEBE_INGRESAR_UN_NUMERO_DE_DOCUMENTO);
        validarObligatorio(idUsuarioCreacion, SE_DEBE_INGRESAR_USUARIO_DE_CREACION);

        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;;
        this.clave = clave;
        this.fechaCreacion = fechaCreacion;
        this.idUsuarioCreacion = idUsuarioCreacion;
    }

}
