package com.ceiba.cuenta.comando.fabrica;

import org.springframework.stereotype.Component;

import com.ceiba.cuenta.comando.ComandoCuenta;
import com.ceiba.cuenta.modelo.entidad.Cuenta;

@Component
public class FabricaCuenta {
	
    public Cuenta crear(ComandoCuenta comandoCuenta) {
        return new Cuenta(comandoCuenta.getId(),
        		comandoCuenta.getNumeroCuenta(),
        		comandoCuenta.getMontoMaximo(),
        		comandoCuenta.getMonto(), 
        		comandoCuenta.getIdCliente(), 
        		comandoCuenta.getFechaCreacion()
        );
    }

}
