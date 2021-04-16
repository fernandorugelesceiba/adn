select IFNULL(group_concat(t.valor_transaccion),"") from transaccion t
where t.id_cuenta_origen = :idCuentaOrigen
and t.fecha_creacion BETWEEN DATE_FORMAT(t.fecha_creacion,'%Y-%m-01') AND  LAST_DAY(t.fecha_creacion)