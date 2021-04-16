update cliente
set nombre = :nombre,
	apellido = :apellido,
	tipo_documento = :tipoDocumento,
	numero_documento = :numeroDocumento
where id = :id