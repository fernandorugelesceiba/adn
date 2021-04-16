package com.ceiba.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ceiba.cliente.puerto.repositorio.RepositorioCliente;
import com.ceiba.cliente.servicio.ServicioActualizarCliente;
import com.ceiba.cliente.servicio.ServicioCrearCliente;
import com.ceiba.cliente.servicio.ServicioEliminarCliente;
import com.ceiba.cuenta.puerto.repositorio.RepositorioCuenta;
import com.ceiba.cuenta.servicio.ServicioActualizarCuenta;
import com.ceiba.cuenta.servicio.ServicioCrearCuenta;
import com.ceiba.cuenta.servicio.ServicioEliminarCuenta;
import com.ceiba.transaccion.puerto.repositorio.RepositorioTransaccion;
import com.ceiba.transaccion.servicio.ServicioCrearTransaccion;
import com.ceiba.usuario.puerto.repositorio.RepositorioUsuario;
import com.ceiba.usuario.servicio.ServicioActualizarUsuario;
import com.ceiba.usuario.servicio.ServicioCrearUsuario;
import com.ceiba.usuario.servicio.ServicioEliminarUsuario;

@Configuration
public class BeanServicio {

	/**
	 * usuarios
	 */

	@Bean
	public ServicioCrearUsuario servicioCrearUsuario(RepositorioUsuario repositorioUsuario) {
		return new ServicioCrearUsuario(repositorioUsuario);
	}

	@Bean
	public ServicioEliminarUsuario servicioEliminarUsuario(RepositorioUsuario repositorioUsuario) {
		return new ServicioEliminarUsuario(repositorioUsuario);
	}

	@Bean
	public ServicioActualizarUsuario servicioActualizarUsuario(RepositorioUsuario repositorioUsuario) {
		return new ServicioActualizarUsuario(repositorioUsuario);
	}

	/**
	 * clientes
	 */
	@Bean
	public ServicioCrearCliente servicioCrearCliente(RepositorioCliente repositorioCliente) {
		return new ServicioCrearCliente(repositorioCliente);
	}

	@Bean
	public ServicioEliminarCliente servicioEliminarCliente(RepositorioCliente repositorioCliente) {
		return new ServicioEliminarCliente(repositorioCliente);
	}

	@Bean
	public ServicioActualizarCliente servicioActualizarCliente(RepositorioCliente repositorioCliente) {
		return new ServicioActualizarCliente(repositorioCliente);
	}

	/**
	 * cuentas
	 */
	@Bean
	public ServicioCrearCuenta servicioCrearCuenta(RepositorioCuenta repositorioCuenta) {
		return new ServicioCrearCuenta(repositorioCuenta);
	}

	@Bean
	public ServicioEliminarCuenta servicioEliminarCuenta(RepositorioCuenta repositorioCuenta) {
		return new ServicioEliminarCuenta(repositorioCuenta);
	}

	@Bean
	public ServicioActualizarCuenta servicioActualizarCuenta(RepositorioCuenta repositorioCuenta) {
		return new ServicioActualizarCuenta(repositorioCuenta);
	}
	
	
	/**
	 * transacciones
	 */
	@Bean
	public ServicioCrearTransaccion servicioCrearTransaccion(RepositorioTransaccion repositorioTransaccion, RepositorioCuenta repositorioCuenta) {
		return new ServicioCrearTransaccion(repositorioTransaccion, repositorioCuenta);
	}

}
