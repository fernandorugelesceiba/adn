insert into transaccion 
(id_cuenta_origen, id_cuenta_destino, valor_transaccion, porcentaje_descuento, fecha_creacion, estado) 
values 
(:idCuentaOrigen, :idCuentaDestino, :valorTransaccion, :porcentajeDescuento, :fechaCreacion, :estado)