select count(1)
from cuenta c 
where TIMESTAMPDIFF(day, c.fecha_creacion, now()) <> 0 
and c.id = :idCuenta