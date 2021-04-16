package com.ceiba.cuenta.puerto.repositorio;

import java.util.List;

import com.ceiba.cuenta.modelo.dto.DtoCuenta;
import com.ceiba.cuenta.modelo.entidad.Cuenta;

public interface RepositorioCuenta {
    /**
     * Permite crear un cliente
     * @param cliente
     * @return el id generado
     */
    Long crear(Cuenta cuenta);

    /**
     * Permite actualizar un cuenta
     * @param cuenta
     */
    void actualizar(Cuenta cuenta);

    /**
     * Permite eliminar un cuenta
     * @param id
     */
    void eliminar(Long id);

    /**
     * Permite validar si existe una cuenta igual
     * @param numeroCuenta
     * @return si existe o no
     */
    boolean existe(String numeroCuenta);
    
	/**
	 * 
	 * @param id
	 * @return
	 */
    List<DtoCuenta> obtenerCuentaSegunId(Long id);
}
