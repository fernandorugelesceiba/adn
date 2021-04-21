insert into usuario(nombre,clave,fecha_creacion) values('test','1234','2021-04-15 18:19:03');

insert into cuenta (id, id_cliente,monto,monto_maximo,numero_cuenta,fecha_creacion)
values (2, 1,500.0,10200.0,'123456787','2021-04-15 18:19:03');

insert into cuenta (id, id_cliente,monto,monto_maximo,numero_cuenta,fecha_creacion)
values (3, 1,600.0,10000.0,'123456789','2021-04-15 18:19:03');

insert into cliente (id, id_usuario_creacion,nombre,apellido,numero_documento,tipo_documento,clave,fecha_creacion)
values (1, 1,'cliente','test','109874651321',1,'1234','2021-04-15 18:19:03');

insert into transaccion(id, id_cuenta_destino,id_cuenta_origen,porcentaje_descuento,valor_transaccion,estado,fecha_creacion)
values(1, 3,1,0.5,100.0,1,'2021-04-15 18:19:03');