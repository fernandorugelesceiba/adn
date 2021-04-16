package com.ceiba.cuenta.adaptador.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import com.ceiba.cuenta.modelo.dto.DtoCuenta;
import com.ceiba.infraestructura.jdbc.MapperResult;

public class MapeoCuenta implements RowMapper<DtoCuenta>, MapperResult {

    @Override
    public DtoCuenta mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Long id = resultSet.getLong("id");
        String numeroCuenta = resultSet.getString("numero_cuenta");
        Double montoMaximo = resultSet.getDouble("monto_maximo");
        Double monto = resultSet.getDouble("monto");
        Long idCliente = resultSet.getLong("id_cliente");
        LocalDateTime fecha = extraerLocalDateTime(resultSet, "fecha_creacion");

        return new DtoCuenta(id, numeroCuenta, montoMaximo, monto, idCliente, fecha);
    }

}
