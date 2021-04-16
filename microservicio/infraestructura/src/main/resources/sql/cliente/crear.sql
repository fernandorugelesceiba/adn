insert into cliente 
(nombre, apellido, tipo_documento, numero_documento, clave, fecha_creacion, id_usuario_creacion) 
values 
(:nombre, :apellido, :tipoDocumento, :numeroDocumento, :clave, :fechaCreacion, :idUsuarioCreacion)