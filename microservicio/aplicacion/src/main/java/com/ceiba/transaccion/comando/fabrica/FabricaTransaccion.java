package com.ceiba.transaccion.comando.fabrica;

import org.springframework.stereotype.Component;

import com.ceiba.transaccion.comando.ComandoTransaccion;
import com.ceiba.transaccion.modelo.entidad.Transaccion;

@Component
public class FabricaTransaccion {
	
    public Transaccion crear(ComandoTransaccion comandoTransaccion) {
        return new Transaccion(comandoTransaccion.getId(),
        		comandoTransaccion.getIdCuentaOrigen(),
        		comandoTransaccion.getIdCuentaDestino(),
        		comandoTransaccion.getValorTransaccion(), 
        		comandoTransaccion.getPorcentajeDescuento(), 
        		comandoTransaccion.getFechaCreacion(),
        		comandoTransaccion.getEstado()
        );
    }

}
