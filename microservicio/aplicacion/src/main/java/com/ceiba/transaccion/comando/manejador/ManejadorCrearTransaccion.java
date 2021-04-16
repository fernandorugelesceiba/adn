package com.ceiba.transaccion.comando.manejador;

import org.springframework.stereotype.Component;

import com.ceiba.ComandoRespuesta;
import com.ceiba.manejador.ManejadorComandoRespuesta;
import com.ceiba.transaccion.comando.ComandoTransaccion;
import com.ceiba.transaccion.comando.fabrica.FabricaTransaccion;
import com.ceiba.transaccion.modelo.entidad.Transaccion;
import com.ceiba.transaccion.servicio.ServicioCrearTransaccion;

@Component
public class ManejadorCrearTransaccion implements ManejadorComandoRespuesta<ComandoTransaccion, ComandoRespuesta<Long>> {

    private final FabricaTransaccion fabricaTransaccion;
    private final ServicioCrearTransaccion servicioCrearTransaccion;

    public ManejadorCrearTransaccion(FabricaTransaccion fabricaTransaccion, ServicioCrearTransaccion servicioCrearTransaccion) {
        this.fabricaTransaccion = fabricaTransaccion;
        this.servicioCrearTransaccion = servicioCrearTransaccion;
    }

    public ComandoRespuesta<Long> ejecutar(ComandoTransaccion comandoTransaccion) {
        Transaccion transaccion = this.fabricaTransaccion.crear(comandoTransaccion);
        return new ComandoRespuesta<>(this.servicioCrearTransaccion.ejecutar(transaccion));
    }

}
