create table usuario (
 id int(11) not null auto_increment,
 nombre varchar(100) not null,
 clave varchar(45) not null,
 fecha_creacion datetime null,
 primary key (id)
);


create table cliente (
 id int(11) not null auto_increment,
 nombre varchar(100) not null,
 apellido varchar(100) not null,
 numero_documento varchar(100) not null,
 tipo_documento int(2) not null,
 clave varchar(45) not null,
 fecha_creacion datetime not null,
 id_usuario_creacion int(11) not null,
 primary key (id)
);


create table cuenta (
 id int(11) not null auto_increment,
 numero_cuenta varchar(100) not null,
 monto double not null,
 monto_maximo double not null,
 id_cliente int(11) not null,
 fecha_creacion datetime not null,
 primary key (id)
);


create table transaccion (
 id int(11) not null auto_increment,
 id_cuenta_origen int(11) not null,
 id_cuenta_destino int(11) not null,
 valor_transaccion double not null,
 porcentaje_descuento double not null,
 estado int(2) not null,
 fecha_creacion datetime not null,
 primary key (id)
);