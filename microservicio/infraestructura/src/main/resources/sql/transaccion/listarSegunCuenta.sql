-- select id, id_cuenta_destino ,id_cuenta_origen ,porcentaje_descuento ,valor_transaccion ,estado, fecha_creacion
-- from transaccion where id_cuenta_origen = :idCuenta or id_cuenta_destino = :idCuenta


select distinct
t.id,
co.numero_cuenta as numeroCuentaOrigen,
cd.numero_cuenta as numeroCuentaDestino,
t.id_cuenta_destino ,
t.id_cuenta_origen ,
t.porcentaje_descuento ,
t.valor_transaccion ,
t.estado,
t.fecha_creacion from transaccion t
inner join cuenta cd on cd.id = t.id_cuenta_destino
inner join cuenta co on co.id = t.id_cuenta_origen
where t.id_cuenta_origen = :idCuenta or t.id_cuenta_destino = :idCuenta
