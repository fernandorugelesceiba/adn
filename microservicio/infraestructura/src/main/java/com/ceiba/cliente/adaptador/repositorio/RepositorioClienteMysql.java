package com.ceiba.cliente.adaptador.repositorio;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.ceiba.cliente.modelo.entidad.Cliente;
import com.ceiba.cliente.puerto.repositorio.RepositorioCliente;
import com.ceiba.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import com.ceiba.infraestructura.jdbc.sqlstatement.SqlStatement;

@Repository
public class RepositorioClienteMysql implements RepositorioCliente {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;

    @SqlStatement(namespace="cliente", value="crear")
    private static String sqlCrear;

    @SqlStatement(namespace="cliente", value="actualizar")
    private static String sqlActualizar;

    @SqlStatement(namespace="cliente", value="eliminar")
    private static String sqlEliminar;

    @SqlStatement(namespace="cliente", value="existe")
    private static String sqlExiste;

    public RepositorioClienteMysql(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
    }

    @Override
    public Long crear(Cliente cliente) {
        return this.customNamedParameterJdbcTemplate.crear(cliente, sqlCrear);
    }

    @Override
    public void eliminar(Long id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);

        this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().update(sqlEliminar, paramSource);
    }

    @Override
    public boolean existe(String numeroDocumento, Short tipoDocumento) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("tipoDocumento", tipoDocumento);
        paramSource.addValue("numeroDocumento", numeroDocumento);

        return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().queryForObject(sqlExiste,paramSource, Boolean.class);
    }

    @Override
    public void actualizar(Cliente cliente) {
        this.customNamedParameterJdbcTemplate.actualizar(cliente, sqlActualizar);
    }

}
