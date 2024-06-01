drop table if exists movie;
drop table if exists saldo;

CREATE TABLE movie (
    id   smallint not null,
    ts timestamptz not null,
    name    TEXT not null
);

