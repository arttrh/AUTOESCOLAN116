create table instrutores
(
    id            bigint       not null auto_increment,
    nome          varchar(100) not null,
    email         varchar(100) not null unique,
    cnh           varchar(11)  not null unique,
    especialidade varchar(20)  not null,
    logradouro    varchar(100) not null,
    numero        varchar(10),
    complemento   varchar(20),
    bairro        varchar(100) not null,
    cidade        varchar(100) not null,
    uf            varchar(2)   not null,
    cep           varchar(9)   not null,

    primary key (id)
);