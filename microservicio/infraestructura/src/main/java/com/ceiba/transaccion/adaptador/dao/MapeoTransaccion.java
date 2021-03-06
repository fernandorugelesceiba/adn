package com.ceiba.transaccion.adaptador.dao;

import com.ceiba.infraestructura.jdbc.MapperResult;
import com.ceiba.transaccion.modelo.dto.DtoTransaccion;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MapeoTransaccion implements RowMapper<DtoTransaccion>, MapperResult {

    @Override
    public DtoTransaccion mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Long id = resultSet.getLong("id");
        Long idCuentaOrigen = resultSet.getLong("id_cuenta_origen");
        Long idCuentaDestino = resultSet.getLong("id_cuenta_destino");
        Double valorTransaccion = resultSet.getDouble("valor_transaccion");
        Double porcentajeDescuento = resultSet.getDouble("porcentaje_descuento");
        String numeroCuentaOrigen = resultSet.getString("numeroCuentaOrigen");
        String numeroCuentaDestino = resultSet.getString("numeroCuentaDestino");
        LocalDateTime fechaCreacion = extraerLocalDateTime(resultSet, "fecha_creacion");
        Short estado = resultSet.getShort("estado");

        return new DtoTransaccion(id, idCuentaOrigen, idCuentaDestino, valorTransaccion, porcentajeDescuento, fechaCreacion, estado, numeroCuentaOrigen, numeroCuentaDestino);
    }

}
