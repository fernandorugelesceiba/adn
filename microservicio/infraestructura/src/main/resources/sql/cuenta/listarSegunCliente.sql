select id, numero_cuenta, id_cliente, monto, monto_maximo, fecha_creacion
from cuenta where id_cliente = :idCliente