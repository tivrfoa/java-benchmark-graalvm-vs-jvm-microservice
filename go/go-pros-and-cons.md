# Go Pros and Cons


## Pros

- Simple

- It doesn't compile with unused imports and variables, eg:
  `./service.go:12:5: "github.com/jackc/pgx/v5" imported as pgx and not used`

- Easily import packages: `go get package_name`

## Cons

- ? Simple ?

- Null pointer

- Error handling

	- it pollutes the business logic a little bit.

- Return error as tuple (?)

	- Rust `Result` seems better.

## Others

### Go set

Go does not have a Set ... but Java also doesn't! xD

Kidding. It has, but it's backed by a HashMap:

src/java.base/share/classes/java/util/HashSet.java

```java
    transient HashMap<E,Object> map;

    // Dummy value to associate with an Object in the backing Map
    static final Object PRESENT = new Object();

    public HashSet() {
        map = new HashMap<>();
    }

    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }
```

You can do like this in Go (credit to Gemini):

```go
  // Create an empty map to store director names (set equivalent)
  directorSet := map[string]struct{}{}
```