package com.ceiba.transaccion.adaptador.dao;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import com.ceiba.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import com.ceiba.infraestructura.jdbc.sqlstatement.SqlStatement;
import com.ceiba.transaccion.modelo.dto.DtoTransaccion;
import com.ceiba.transaccion.puerto.dao.DaoTransaccion;

@Component
public class DaoTransaccionMysql implements DaoTransaccion {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;

    @SqlStatement(namespace="transaccion", value="listarSegunCuenta")
    private static String sqlListarSegunCuenta;

    public DaoTransaccionMysql(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
    }

    @Override
    public List<DtoTransaccion> listar(Long idCuenta) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("idCuenta", idCuenta);
        
        return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().query(sqlListarSegunCuenta, paramSource, new MapeoTransaccion());
    }
}
