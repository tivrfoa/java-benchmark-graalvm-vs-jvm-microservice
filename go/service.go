// Code mostly generated from Gemini
// prompt: I need Go code to call those two Go endpoints that you created earlier.
// It will a different Go program that will call the address and phone endpoints
package main

import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"io"
	"net/http"
	"sync/atomic"
)

func NewClient(name string, age uint32, monthSalary float64, favoriteDirector string) Client {
	return Client{
		Name:             name,
		Age:              age,
		MonthSalary:      monthSalary,
		FavoriteDirector: favoriteDirector,
	}
}

func NewLoanOption(years int, loan float64, monthlyInstallment float64) LoanOption {
	return LoanOption{
		Years:              years,
		Loan:               loan,
		MonthlyInstallment: monthlyInstallment,
	}
}

func calculateLoanOptions(client Client) []LoanOption {
	options := []LoanOption{} // Empty slice to store LoanOption instances
	for year := 1; year <= 3; year++ {
		monthlyInstallment := 0.03 * client.MonthSalary // Use dot notation for struct fields
		maxLoan := monthlyInstallment * float64(12*year)
		interest := 0.07 * maxLoan
		loanAmount := maxLoan - interest
		options = append(options, NewLoanOption(year, loanAmount, monthlyInstallment))
	}
	return options
}

func NewResponseLoanOptions(client Client, loanOptions []LoanOption) ResponseLoanOptions {
	return ResponseLoanOptions{
		Client:      client,
		LoanOptions: loanOptions,
	}
}

// Define a constant slice of LoanOption to represent no loan options
var NoLoanOptionsAvailable = []LoanOption{} // Empty slice

var clients = []Client{
	NewClient("A", 18, 3000, "J J Abrams"),
	NewClient("B", 19, 3500, "Louis Leterrier"),
	NewClient("C", 20, 4000, "Rob Marshall"),
	NewClient("D", 21, 4500, "Joss Whedon"),
	NewClient("E", 22, 5000, "Anthony Russo & Joe Russo"),
	NewClient("F", 23, 6000, "Sam Raimi"),
	NewClient("G", 24, 7000, "James Mangold"),
	NewClient("H", 25, 8000, "Gore Verbinski"),
	NewClient("I", 26, 9000, "Zack Snyder & Joss Whedon"),
	NewClient("J", 27, 10000, "Rian Johnson"),
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

func getServiceHandler(w http.ResponseWriter, r *http.Request) {
	clientID := getClientId()
	client := clients[clientID]

	phones, err := getPhones(clientID)
	if err != nil {
		fmt.Println("Error fetching phones:", err)
		return
	}
	client.Phones = phones

	loanOptions := NoLoanOptionsAvailable
	if client.Age >= 18 {
		address, err := getAddress(clientID)
		if err != nil {
			fmt.Println("Error fetching address:", err)
			return
		}
		client.Address = *address
		loanOptions = calculateLoanOptions(client)
	} else {
		fmt.Println("BUG: this should be unreachable")
		address, err := getAddress(client.GuardianID)
		if err != nil {
			fmt.Println("Error fetching address:", err)
			return
		}
		client.Address = *address
	}

	response := NewResponseLoanOptions(client, loanOptions)
	jsonResponse, err := json.Marshal(response)
	if err != nil {
		fmt.Println("Error marshalling client data:", err)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.Write(jsonResponse)
}

func callAPI(url string) ([]byte, error) {
	req, err := http.NewRequest(http.MethodGet, url, nil)
	if err != nil {
		return nil, err
	}

	http_client := &http.Client{}
	resp, err := http_client.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("unexpected status code: %d", resp.StatusCode)
	}

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, err
	}
	return body, nil
}

func getPhones(clientID uint32) ([]Phone, error) {
	phonesURL := "http://localhost:8080/phones"
	url := fmt.Sprintf("%s/%d", phonesURL, clientID) // Format URL with client ID

	body, err := callAPI(url)
	if err != nil {
		return nil, err
	}

	var phones []Phone
	err = json.Unmarshal(body, &phones)
	if err != nil {
		return nil, err
	}

	return phones, nil
}

func getAddress(clientID uint32) (*Address, error) {
	addressURL := "http://localhost:8080/address"
	url := fmt.Sprintf("%s/%d", addressURL, clientID)

	body, err := callAPI(url)
	if err != nil {
		return nil, err
	}

	var address Address
	err = json.Unmarshal(body, &address)
	if err != nil {
		return nil, err
	}

	return &address, nil
}

// Created by Gemini. I hope it's correct xD
var counter uint32 = 0

func getClientId() uint32 {
	for {
		oldVal := atomic.LoadUint32(&counter)
		newVal := (oldVal + 1) % 10
		if atomic.CompareAndSwapUint32(&counter, oldVal, newVal) {
			return oldVal // Return the old value before increment
		}
	}
}

func getClientFavoriteDirectorMovies(client Client, movies []Movie) ClientFavoriteDirectorMovies {
	// create a map just for performance test
	moviesByDirector := make(map[string][]Movie)

	for _, movie := range movies {
		moviesByDirector[movie.Director] = append(moviesByDirector[movie.Director], movie)
	}

	return ClientFavoriteDirectorMovies{
		Client: client,
		Movies: moviesByDirector[client.FavoriteDirector],
	}
}

func dbHandler(w http.ResponseWriter, r *http.Request, pool *pgxpool.Pool) {
	client := clients[getClientId()]
	movies := queryMovies(pool)

	clientFavoriteDirectorMovies := getClientFavoriteDirectorMovies(client, movies)

	jsonResponse, err := json.Marshal(clientFavoriteDirectorMovies)
	if err != nil {
		fmt.Println("Error marshalling client data:", err)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.Write(jsonResponse)
}

func main() {
	pool := CreateConnectionPool()

	http.HandleFunc("/hello", getServiceHandler)
	http.HandleFunc("/db", func(w http.ResponseWriter, r *http.Request) {
		dbHandler(w, r, pool)
	})

	port := ":8081"
	fmt.Printf("Server listening on port %s\n", port)
	err := http.ListenAndServe(port, nil)
	if err != nil {
		panic(err)
	}
}
