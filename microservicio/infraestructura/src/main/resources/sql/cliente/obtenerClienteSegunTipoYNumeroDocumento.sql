select * from cliente 
where tipo_documento = :tipoDocumento and numero_documento = :numeroDocumento
limit 1