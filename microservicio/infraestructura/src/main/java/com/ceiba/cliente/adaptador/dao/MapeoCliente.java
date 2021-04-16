package com.ceiba.cliente.adaptador.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import com.ceiba.cliente.modelo.dto.DtoCliente;
import com.ceiba.infraestructura.jdbc.MapperResult;

public class MapeoCliente implements RowMapper<DtoCliente>, MapperResult {

    @Override
    public DtoCliente mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Long id = resultSet.getLong("id");
        Short tipoDocumento = resultSet.getShort("tipo_documento");
        String numeroDocumento = resultSet.getString("numero_documento");
        String nombre = resultSet.getString("nombre");
        String apellido = resultSet.getString("apellido");
        //String clave = resultSet.getString("clave");
        LocalDateTime fecha = extraerLocalDateTime(resultSet, "fecha_creacion");
        Long idUsuarioCreacion = resultSet.getLong("id_usuario_creacion");

        return new DtoCliente(id, nombre, apellido, tipoDocumento, numeroDocumento, fecha, idUsuarioCreacion);
    }

}
