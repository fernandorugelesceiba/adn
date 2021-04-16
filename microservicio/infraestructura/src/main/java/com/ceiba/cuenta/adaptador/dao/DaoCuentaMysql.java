package com.ceiba.cuenta.adaptador.dao;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import com.ceiba.cuenta.modelo.dto.DtoCuenta;
import com.ceiba.cuenta.puerto.dao.DaoCuenta;
import com.ceiba.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import com.ceiba.infraestructura.jdbc.sqlstatement.SqlStatement;

@Component
public class DaoCuentaMysql implements DaoCuenta {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;

    @SqlStatement(namespace="cuenta", value="listar")
    private static String sqlListar;
    
    @SqlStatement(namespace="cuenta", value="listarSegunCliente")
    private static String sqlListarSegunCliente;
    
    public DaoCuentaMysql(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
    }

    @Override
    public List<DtoCuenta> listar() {
        return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().query(sqlListar, new MapeoCuenta());
    }

	@Override
	public List<DtoCuenta> listarCuentasPorCliente(Long cliente) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("idCliente", cliente);
        
		return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().query(sqlListarSegunCliente, paramSource, new MapeoCuenta());
	}
}
