package com.ceiba.cuenta.adaptador.repositorio;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.ceiba.cuenta.adaptador.dao.MapeoCuenta;
import com.ceiba.cuenta.modelo.dto.DtoCuenta;
import com.ceiba.cuenta.modelo.entidad.Cuenta;
import com.ceiba.cuenta.puerto.repositorio.RepositorioCuenta;
import com.ceiba.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import com.ceiba.infraestructura.jdbc.sqlstatement.SqlStatement;

@Repository
public class RepositorioCuentaMysql implements RepositorioCuenta {

	private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;

	@SqlStatement(namespace = "cuenta", value = "crear")
	private static String sqlCrear;

	@SqlStatement(namespace = "cuenta", value = "actualizar")
	private static String sqlActualizar;

	@SqlStatement(namespace = "cuenta", value = "eliminar")
	private static String sqlEliminar;

	@SqlStatement(namespace = "cuenta", value = "existe")
	private static String sqlExiste;

	@SqlStatement(namespace = "cuenta", value = "listarSegunId")
	private static String sqlListarSegunId;

	public RepositorioCuentaMysql(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate) {
		this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
	}

	@Override
	public Long crear(Cuenta cuenta) {
		return this.customNamedParameterJdbcTemplate.crear(cuenta, sqlCrear);
	}

	@Override
	public void eliminar(Long id) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("id", id);

		this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().update(sqlEliminar, paramSource);
	}

	@Override
	public boolean existe(String numeroCuenta) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("numeroCuenta", numeroCuenta);

		return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().queryForObject(sqlExiste,
				paramSource, Boolean.class);
	}

	@Override
	public void actualizar(Cuenta cuenta) {
		this.customNamedParameterJdbcTemplate.actualizar(cuenta, sqlActualizar);
	}

	@Override
	public List<DtoCuenta> obtenerCuentaSegunId(Long id) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("idCuenta", id);

		return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
				.query(sqlListarSegunId, paramSource, new MapeoCuenta());
	}

}
