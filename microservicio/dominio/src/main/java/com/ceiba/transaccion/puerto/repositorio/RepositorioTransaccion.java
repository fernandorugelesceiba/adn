package com.ceiba.transaccion.puerto.repositorio;

import com.ceiba.transaccion.modelo.entidad.Transaccion;

import java.time.LocalDateTime;
import java.util.List;

public interface RepositorioTransaccion {
    /**
     * Permite crear una transaccion
     * @param transaccion
     * @return el id generado
     */
    Long crear(Transaccion transaccion);

    /**
     * @param idCuenta
     * @return si la fecha de creacion de una cuenta lleva mas de un dia activa, es valida
     */
    default boolean verificarFechaValidesEnCuenta(Long idCuenta) {
        return false;
    }

    /**
     * 
     * @param idCuenta
     * @return cantidad de transacciones de una cuenta
     */
    List<Double> obtenerCantidadDeTransaccionesSegunCuentaEnElMesYMontoTotal(Long idCuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * 
     * @param idCuenta
     * @return obtiene el monto maximo de un cuenta segun su id
     */
    Double obtenerElMontoMaximoDeUnCuentaSegunSuId(Long idCuenta);
}
