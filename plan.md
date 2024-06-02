# Compare Java GraalVM Native Image x OpenJDK x OpenJ9 AOT + class data sharing x Go



## |x| Add Quarkus Reactive

https://github.com/quarkusio/quarkus-super-heroes/blob/c68c84d362a376fde659c9b9cf6af1b80c6ea530/rest-fights/src/main/java/io/quarkus/sample/superheroes/fight/service/FightService.java#L100-L114

https://quarkus.io/guides/getting-started-reactive

https://quarkus.io/guides/mutiny-primer

https://quarkus.io/guides/reactive-event-bus

https://stackoverflow.com/questions/73605586/is-quarkus-reactive-client-really-reactive

https://dzone.com/articles/invoking-rest-apis-asynchronously-with-quarkus

https://smallrye.io/smallrye-mutiny/2.0.0/guides/combining-items/



## || Add Postgres database connection

Just one table with the 15 most expensive movies.

Goal:
	- test db driver performance
	- test set
	- test hash map

1) query all rows
2) create a set of directors
3) create a `HashMap<Director, List<Movie>>`

It's just for testing folks ... if want to retrieve the movies of a
specific director, you would just pass the director for the `where`
SQL condition.

You also don't need a set. You could do:

```java
var map = new HashMap<Director, ArrayList<Movie>>();
for (var movie : movies) {
	var l = map.computeIfAbsent(movie.director, d -> new ArrayList<>());
	l.add(movie);
}
```

Well ... Go does not have a Set ... but Java also doesn't! xD

Kidding. It has, but it's backed by a HashMap:

src/java.base/share/classes/java/util/HashSet.java

```java
    transient HashMap<E,Object> map;

    public HashSet() {
        map = new HashMap<>();
    }

    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }

```

I'll do like this in Go (credit to Gemini):

```go
  // Create an empty map to store director names (set equivalent)
  directorSet := map[string]struct{}{}
```

### || Go

https://pkg.go.dev/database/sql

https://golang.org/s/sqldrivers

I used pgx (most stars xD)
https://github.com/jackc/pgx

https://www.reddit.com/r/golang/comments/162ieb1/need_suggestions_for_choosing_driver_for_postgres/

https://pkg.go.dev/github.com/jackc/pgx/v4/pgxpool

https://github.com/jackc/pgx/wiki/Numeric-and-decimal-support

https://stackoverflow.com/questions/61704842/how-to-scan-a-queryrow-into-a-struct-with-pgx

https://stackoverflow.com/questions/31946344/why-does-go-treat-a-postgresql-numeric-decimal-columns-as-uint8


## || Add Rust
