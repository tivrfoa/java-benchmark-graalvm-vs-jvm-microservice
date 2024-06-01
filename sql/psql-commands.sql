/*

$ psql -U admin -h localhost -d bench
psql (14.11 (Ubuntu 14.11-0ubuntu0.22.04.1), server 16.2 (Debian 16.2-1.pgdg120+2))
WARNING: psql major version 14, server major version 16.
         Some psql features might not work.
Type "help" for help.

bench=# \d
       List of relations
 Schema | Name  | Type  | Owner 
--------+-------+-------+-------
 public | movie | table | admin
(1 row)

bench=# select * from movie;
 id | ts | name 
----+----+------
(0 rows)

*/

insert into movie values(1, current_timestamp, 'ola');

