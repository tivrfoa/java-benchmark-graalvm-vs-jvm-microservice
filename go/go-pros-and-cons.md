# Go Pros and Cons


## Pros

- Simple

- It doesn't compile with unused imports and variables, eg:
  `./service.go:12:5: "github.com/jackc/pgx/v5" imported as pgx and not used`

## Cons

- ? Simple ?

- Null pointer

- Error handling

	- it pollutes the business logic a little bit.

- Return error as tuple (?)

	- Rust `Result` is much better.