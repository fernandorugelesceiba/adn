package com.ceiba.transaccion.adaptador.repositorio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.ceiba.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import com.ceiba.infraestructura.jdbc.sqlstatement.SqlStatement;
import com.ceiba.transaccion.modelo.entidad.Transaccion;
import com.ceiba.transaccion.puerto.repositorio.RepositorioTransaccion;

@Repository
public class RepositorioTransaccionMysql implements RepositorioTransaccion {

	private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;

	@SqlStatement(namespace = "transaccion", value = "crear")
	private static String sqlCrear;

	@SqlStatement(namespace = "transaccion", value = "verificarFechaValidesEnCuenta")
	private static String sqlVerificarFechaValidesEnCuenta;
	
	@SqlStatement(namespace = "transaccion", value = "obtenerCantidadDeTransaccionesSegunCuentaEnElMes")
	private static String sqlObtenerCantidadDeTransaccionesSegunCuentaEnElMes;
	
	@SqlStatement(namespace = "transaccion", value = "obtenerMontoMaximoSegunIdCuenta")
	private static String sqlObtenerMontoMaximoSegunIdCuenta;

	public RepositorioTransaccionMysql(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate) {
		this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
	}

	@Override
	public Long crear(Transaccion transaccion) {
		return this.customNamedParameterJdbcTemplate.crear(transaccion, sqlCrear);
	}
	
	@Override
	public boolean verificarFechaValidesEnCuenta(Long idCuenta) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("idCuenta", idCuenta);

		return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
				.queryForObject(sqlVerificarFechaValidesEnCuenta, paramSource, Boolean.class);
	}
	
	@Override
	public List<Double> obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(Long idCuenta) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("idCuentaOrigen", idCuenta);

		String resultado = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
				.queryForObject(sqlObtenerCantidadDeTransaccionesSegunCuentaEnElMes, paramSource, String.class);
		
		List<Double> listaValoresDeTransaccion = new ArrayList<>();
		String[] valoresDeTransaccion = resultado.split(",");
		for(String valor : valoresDeTransaccion) {
			listaValoresDeTransaccion.add(new BigDecimal(valor.length() > 0 ? valor.length() : 0).doubleValue());
		}
		
		return listaValoresDeTransaccion;
	}

	@Override
	public Double obtenerElMontoMaximoDeUnCuentaSegunSuId(Long idCuenta) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("idCuenta", idCuenta);

		return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
				.queryForObject(sqlObtenerMontoMaximoSegunIdCuenta, paramSource, Double.class);
	}
}
