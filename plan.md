# Compare Java GraalVM Native Image x OpenJDK x OpenJ9 AOT + class data sharing x Go

	- |x| Compare JSON endpoint
	- || Compare database endpoint
	- || Comparing running inside Docker with restricted resources

## |x| Quarkus Hibernate Reactive with Panache

  - It consumes much more memory
  - Takes much longer to get hot

## |x| Add Quarkus Reactive

https://github.com/quarkusio/quarkus-super-heroes/blob/c68c84d362a376fde659c9b9cf6af1b80c6ea530/rest-fights/src/main/java/io/quarkus/sample/superheroes/fight/service/FightService.java#L100-L114

https://quarkus.io/guides/getting-started-reactive

https://quarkus.io/guides/mutiny-primer

https://quarkus.io/guides/reactive-event-bus

https://stackoverflow.com/questions/73605586/is-quarkus-reactive-client-really-reactive

https://dzone.com/articles/invoking-rest-apis-asynchronously-with-quarkus

https://smallrye.io/smallrye-mutiny/2.0.0/guides/combining-items/

https://www.reddit.com/r/java/comments/fxejpo/reactor_vs_rxjava3_2020_edition/

https://www.reddit.com/r/java/comments/xf4pl7/reactor_bad_loom_good_but_how_will_the_landscape/

[JEP 428: Structured Concurrency (Incubator)](https://openjdk.org/jeps/428)

https://blog.krecan.net/2013/12/25/completablefutures-why-to-use-async-methods/

https://x.com/jponge/status/1450120749621071882


## || Add Postgres database connection

  - || Quarkus
    - |x| Agroal jdbc
    - || Hibernate
  - |x| Go

Just one table with the 15 most expensive movies.

Goal:
	- test db driver performance
	- test hash map

1) query all rows
2) create a `HashMap<Director, List<Movie>>`

It's just for testing folks ... if want to retrieve the movies of a
specific director, you would just pass the director for the `where`
SQL condition.

### Quarkus

[Agroal Sorry, acquisition timeout](https://groups.google.com/g/quarkus-dev/c/uxCEs4Bxk0I)

Nice Quarkus config!

```properties
quarkus.datasource.jdbc.enable-metrics=true
```

`2024-06-02 17:35:02,095 WARN  [io.agr.pool] (executor-thread-61) Datasource '<default>': JDBC resources leaked: 1 ResultSet(s) and 0 Statement(s)`

https://quarkus.io/guides/hibernate-reactive-panache

https://quarkus.io/guides/reactive-sql-clients

https://smallrye.io/smallrye-mutiny/2.5.1/reference/uni-and-multi/

### Go

https://pkg.go.dev/database/sql

https://golang.org/s/sqldrivers

I used pgx (most stars xD)
https://github.com/jackc/pgx

https://www.reddit.com/r/golang/comments/162ieb1/need_suggestions_for_choosing_driver_for_postgres/

https://pkg.go.dev/github.com/jackc/pgx/v4/pgxpool

https://github.com/jackc/pgx/wiki/Numeric-and-decimal-support

https://stackoverflow.com/questions/61704842/how-to-scan-a-queryrow-into-a-struct-with-pgx

https://stackoverflow.com/questions/31946344/why-does-go-treat-a-postgresql-numeric-decimal-columns-as-uint8

https://medium.com/@neelkanthsingh.jr/understanding-database-connection-pools-and-the-pgx-library-in-go-3087f3c5a0c


## || Add Rust


## || GraalVM Native Image for Vertx

I was not able to do it. Maybe someone can send a PR. =)

https://graalvm.github.io/native-build-tools/latest/maven-plugin.html

https://github.com/cch0/vertx-graal-native-image

https://blog.valensas.com/our-adventures-with-graalvm-the-good-the-bad-and-the-ugly-cbf6d6e7f0d9
