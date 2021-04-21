select * from transaccion
where id_cuenta_origen = :idCuentaOrigen
and fecha_creacion BETWEEN :fechaInicio AND  :fechaFin