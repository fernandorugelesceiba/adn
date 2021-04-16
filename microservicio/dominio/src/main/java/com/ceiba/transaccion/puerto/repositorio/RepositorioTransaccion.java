package com.ceiba.transaccion.puerto.repositorio;

import java.util.List;

import com.ceiba.transaccion.modelo.entidad.Transaccion;

public interface RepositorioTransaccion {
    /**
     * Permite crear una transaccion
     * @param transaccion
     * @return el id generado
     */
    Long crear(Transaccion transaccion);

    /**
     * 
     * @param idCuenta
     * @return si la fecha de creacion de una cuenta lleva mas de un dia activa, es valida
     */
    boolean verificarFechaValidesEnCuenta(Long idCuenta);

    /**
     * 
     * @param idCuenta
     * @return cantidad de transacciones de una cuenta
     */
    List<Double> obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(Long idCuenta);
    
    /**
     * 
     * @param idCuenta
     * @return obtiene el monto maximo de un cuenta segun su id
     */
    Double obtenerElMontoMaximoDeUnCuentaSegunSuId(Long idCuenta);
}
