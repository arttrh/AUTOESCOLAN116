create table usuarios
(
    id     bigint       not null auto_increment,
    login  varchar(100) not null unique,
    senha  varchar(255) not null,
    ativo  tinyint      not null default 1,
    perfil varchar(10)  not null,

    primary key (id)
);