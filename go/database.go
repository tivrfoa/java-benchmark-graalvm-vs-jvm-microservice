package main

import (
	"context"
	"fmt"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"os"
	"time"
)

// https://medium.com/@neelkanthsingh.jr/understanding-database-connection-pools-and-the-pgx-library-in-go-3087f3c5a0c
func config() *pgxpool.Config {
	const defaultMaxConns = int32(25)
	const defaultMinConns = int32(5)
	const defaultMaxConnLifetime = time.Hour
	const defaultMaxConnIdleTime = time.Minute * 30
	const defaultHealthCheckPeriod = time.Minute
	const defaultConnectTimeout = time.Second * 5

	// Your own Database URL
	const DATABASE_URL string = "postgres://admin:123@localhost:5432/bench"

	dbConfig, err := pgxpool.ParseConfig(DATABASE_URL)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Failed to create a config, error: ", err)
	}

	dbConfig.MaxConns = defaultMaxConns
	dbConfig.MinConns = defaultMinConns
	dbConfig.MaxConnLifetime = defaultMaxConnLifetime
	dbConfig.MaxConnIdleTime = defaultMaxConnIdleTime
	dbConfig.HealthCheckPeriod = defaultHealthCheckPeriod
	dbConfig.ConnConfig.ConnectTimeout = defaultConnectTimeout

	dbConfig.BeforeAcquire = func(ctx context.Context, c *pgx.Conn) bool {
		// fmt.Println("Before acquiring the connection pool to the database!!")
		return true
	}

	dbConfig.AfterRelease = func(c *pgx.Conn) bool {
		// fmt.Println("After releasing the connection pool to the database!!")
		return true
	}

	dbConfig.BeforeClose = func(c *pgx.Conn) {
		// fmt.Println("Closed the connection pool to the database!!")
	}

	return dbConfig
}

func CreateConnectionPool() *pgxpool.Pool {
	// Create database connection
	connPool, err := pgxpool.NewWithConfig(context.Background(), config())
	if err != nil {
		fmt.Fprintf(os.Stderr, "Error while creating connection to the database!!")
	}

	connection, err := connPool.Acquire(context.Background())
	if err != nil {
		fmt.Fprintf(os.Stderr, "Error while acquiring connection from the database pool!!")
	}
	defer connection.Release()

	err = connection.Ping(context.Background())
	if err != nil {
		fmt.Fprintf(os.Stderr, "Could not ping database")
	}

	return connPool
}

func queryMovies(pool *pgxpool.Pool) []Movie {
	rows, _ := pool.Query(context.Background(), "select title, year, cost, director from movie")
	movies, err := pgx.CollectRows(rows, pgx.RowToStructByName[Movie])
	if err != nil {
		fmt.Printf("CollectRows error: %v", err)
		return []Movie{}
	}
	return movies
}
