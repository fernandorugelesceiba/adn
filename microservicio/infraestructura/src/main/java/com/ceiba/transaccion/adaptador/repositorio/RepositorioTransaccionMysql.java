package com.ceiba.transaccion.adaptador.repositorio;

import com.ceiba.cuenta.adaptador.dao.MapeoCuenta;
import com.ceiba.cuenta.modelo.dto.DtoCuenta;
import com.ceiba.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import com.ceiba.infraestructura.jdbc.sqlstatement.SqlStatement;
import com.ceiba.transaccion.adaptador.dao.MapeoTransaccion;
import com.ceiba.transaccion.modelo.dto.DtoTransaccion;
import com.ceiba.transaccion.modelo.entidad.Transaccion;
import com.ceiba.transaccion.puerto.repositorio.RepositorioTransaccion;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
		boolean esValido = true;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("idCuenta", idCuenta);

		List<DtoCuenta> resultado = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
				.query(sqlVerificarFechaValidesEnCuenta, paramSource, new MapeoCuenta());

		if(!resultado.isEmpty()){
			LocalDateTime fechaCreacionRegistro = resultado.get(0).getFechaCreacion();
			LocalDateTime fechaActual = LocalDateTime.now();
			LocalDateTime tempDateTime = LocalDateTime.from(fechaCreacionRegistro);
			long diasDiferencia = tempDateTime.until( fechaActual, ChronoUnit.DAYS );

			if(diasDiferencia <= 0){
				esValido = false;
			}
		}
		return esValido;
	}
	
	@Override
	public List<Double> obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(Long idCuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("idCuentaOrigen", idCuenta);
		paramSource.addValue("fechaInicio", fechaInicio);
		paramSource.addValue("fechaFin", fechaFin);

		List<DtoTransaccion> resultado =  this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().query(sqlObtenerCantidadDeTransaccionesSegunCuentaEnElMes, paramSource, new MapeoTransaccion());
		List<Double> listaValoresDeTransaccion = new ArrayList<>();

		if(!resultado.isEmpty()){
			for(DtoTransaccion transaccion : resultado) {
				Double valorDouble = BigDecimal.valueOf(transaccion.getValorTransaccion()).doubleValue();
				listaValoresDeTransaccion.add(valorDouble);
			}
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
