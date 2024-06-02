// Code mostly generated from Gemini
// prompt: I need Go code to call those two Go endpoints that you created earlier.
// It will a different Go program that will call the address and phone endpoints
package main

import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/jackc/pgx/v5"
	"io"
	"net/http"
	"os"
	"sync/atomic"
)

// Define a struct to hold client information
type Client struct {
	// ID          uint32  `json:"id"`
	Name        string  `json:"name"`
	Age         uint32  `json:"age"`
	GuardianID  uint32  `json:"guardianID"`
	MonthSalary float64 `json:"monthSalary"`
	Address     Address `json:"address"`
	Phones      []Phone `json:"phones"`
}

func NewClient(name string, age uint32, monthSalary float64) Client {
	return Client{
		// Set the relevant fields based on the provided arguments
		Name:        name,
		Age:         age,
		MonthSalary: monthSalary,
		// You can also set default values for other fields here if needed
	}
}

// Address struct to hold address information (same as before)
type Address struct {
	Street  string `json:"street"`
	Number  int    `json:"number"`
	State   string `json:"state"`
	Country string `json:"country"`
}

// Phone struct to hold phone number information (same as before)
type Phone struct {
	DDD    int `json:"ddd"`
	Number int `json:"number"`
}

type LoanOption struct {
	Years int `json:"years"`

	// Gemini suggested loanAmount, which is better! But let's keep the same as the Java version.
	Loan               float64 `json:"loan"`
	MonthlyInstallment float64 `json:"monthlyInstallment"`
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

type ResponseLoanOptions struct {
	Client      Client       `json:"client"`
	LoanOptions []LoanOption `json:"loanOptions"`
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
	NewClient("A", 18, 3000),
	NewClient("B", 19, 3500),
	NewClient("C", 20, 4000),
	NewClient("D", 21, 4500),
	NewClient("E", 22, 5000),
	NewClient("F", 23, 6000),
	NewClient("G", 24, 7000),
	NewClient("H", 25, 8000),
	NewClient("I", 26, 9000),
	NewClient("J", 27, 10000),
}

type Movie struct {
	Title    string  `pg:"title,notnull"`
	Year     int16   `pg:"year"`
	Cost     float64 `pg:"cost"`
	Director string  `pg:"director"`
}

func testPostgresDb() {
	postgresURL := "postgres://admin:123@localhost:5432/bench"
	conn, err := pgx.Connect(context.Background(), postgresURL)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Unable to connect to database: %v\n", err)
		os.Exit(1)
	}
	defer conn.Close(context.Background())

	// var name string
	// err = conn.QueryRow(context.Background(), "select name from movie").Scan(&name)
	// if err != nil {
	//     fmt.Fprintf(os.Stderr, "QueryRow failed: %v\n", err)
	// }
	// fmt.Println(name)
	rows, _ := conn.Query(context.Background(), "select title, year, cost, director from movie")
	movies, err := pgx.CollectRows(rows, pgx.RowToStructByName[Movie])
	if err != nil {
		fmt.Printf("CollectRows error: %v", err)
		return
	}

	// Create an empty map to store director names (set equivalent)
	directorSet := map[string]struct{}{}

	for _, movie := range movies {
		// fmt.Printf("%s: %s\n", movie.Title, movie.Director)
		directorSet[movie.Director] = struct{}{} // Empty struct for value (doesn't matter)
	}

	// fmt.Println(directorSet)
	// for directorName, _ := range directorSet {
	//     fmt.Println(directorName)
	// }
}

func testPostgresDbPool() {
	postgresURL := "postgres://admin:123@localhost:5432/bench"
	pool, err := pgxpool.New(context.Background(), postgresURL)
	// conn, err := pgx.Connect(context.Background(), postgresURL)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Unable to connect to database: %v\n", err)
		os.Exit(1)
	}
	defer conn.Close(context.Background())

	// var name string
	// err = conn.QueryRow(context.Background(), "select name from movie").Scan(&name)
	// if err != nil {
	//     fmt.Fprintf(os.Stderr, "QueryRow failed: %v\n", err)
	// }
	// fmt.Println(name)
	rows, _ := conn.Query(context.Background(), "select title, year, cost, director from movie")
	movies, err := pgx.CollectRows(rows, pgx.RowToStructByName[Movie])
	if err != nil {
		fmt.Printf("CollectRows error: %v", err)
		return
	}

	// Create an empty map to store director names (set equivalent)
	directorSet := map[string]struct{}{}

	for _, movie := range movies {
		// fmt.Printf("%s: %s\n", movie.Title, movie.Director)
		directorSet[movie.Director] = struct{}{} // Empty struct for value (doesn't matter)
	}

	// fmt.Println(directorSet)
	// for directorName, _ := range directorSet {
	//     fmt.Println(directorName)
	// }
}

func main() {
	testPostgresDb()

	// Register the handler for the GET request
	http.HandleFunc("/hello", getServiceHandler)

	// Start the server on port 8080
	fmt.Println("Server listening on port 8081")
	err := http.ListenAndServe(":8081", nil)
	if err != nil {
		panic(err)
	}
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
