package com.ceiba.cliente.adaptador.dao;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import com.ceiba.cliente.modelo.dto.DtoCliente;
import com.ceiba.cliente.puerto.dao.DaoCliente;
import com.ceiba.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import com.ceiba.infraestructura.jdbc.sqlstatement.SqlStatement;

@Component
public class DaoClienteMysql implements DaoCliente {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;

    @SqlStatement(namespace="cliente", value="listar")
    private static String sqlListar;
    
    @SqlStatement(namespace="cliente", value="obtenerClienteSegunTipoYNumeroDocumento")
    private static String sqlClienteSegunTipoDocumentoYDocumento;

    public DaoClienteMysql(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
    }

    @Override
    public List<DtoCliente> listar() {
        return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().query(sqlListar, new MapeoCliente());
    }

	@Override
	public List<DtoCliente> obtenerClienteSegunTipoYNumeroDocumento(Short tipoDocumento, String numeroDocumento) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("tipoDocumento", tipoDocumento);
        paramSource.addValue("numeroDocumento", numeroDocumento);
        
		return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().query(sqlClienteSegunTipoDocumentoYDocumento, paramSource, new MapeoCliente());
	}
}
