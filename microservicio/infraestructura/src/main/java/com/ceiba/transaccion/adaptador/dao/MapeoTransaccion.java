package com.ceiba.transaccion.adaptador.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import com.ceiba.infraestructura.jdbc.MapperResult;
import com.ceiba.transaccion.modelo.dto.DtoTransaccion;

public class MapeoTransaccion implements RowMapper<DtoTransaccion>, MapperResult {

    @Override
    public DtoTransaccion mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Long id = resultSet.getLong("id");
        Long idCuentaOrigen = resultSet.getLong("id_cuenta_origen");
        Long idCuentaDestino = resultSet.getLong("id_cuenta_destino");
        Double valorTransaccion = resultSet.getDouble("valor_transaccion");
        Double porcentajeDescuento = resultSet.getDouble("porcentaje_descuento");
        LocalDateTime fechaCreacion = extraerLocalDateTime(resultSet, "fecha_creacion");
        Short estado = resultSet.getShort("estado");
        
        return new DtoTransaccion(id, idCuentaOrigen, idCuentaDestino, valorTransaccion, porcentajeDescuento, fechaCreacion, estado);
    }

}
