select id, id_cuenta_destino ,id_cuenta_origen ,porcentaje_descuento ,valor_transaccion ,estado, fecha_creacion
from transaccion where id_cuenta_origen = :idCuenta or id_cuenta_destino = :idCuenta
