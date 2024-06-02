// Code generated from Gemini
// prompt: Create a basic Go server that has a GET endpoint that returns an
// address struct containing: street, number, state and country.
package main

import (
	"encoding/json"
	"fmt"
	"net/http"
)

// Address struct to hold address information
type Address struct {
	Street  string `json:"street"`
	Number  int    `json:"number"`
	State   string `json:"state"`
	Country string `json:"country"`
}

// Define the address data
var address = Address{
	Street:  "123 Main St",
	Number:  42,
	State:   "CA",
	Country: "USA",
}

// Function to handle GET requests for the address
func getAddressHandler(w http.ResponseWriter, r *http.Request) {
	// clientID := r.PathValue("client_id")
	// Set content type as JSON
	w.Header().Set("Content-Type", "application/json")

	// Marshal the address struct to JSON
	jsonBytes, err := json.Marshal(address)
	if err != nil {
		fmt.Fprintf(w, "Error marshalling address to JSON: %v", err)
		return
	}

	// Write the JSON response
	w.Write(jsonBytes)
}

type Phone struct {
	DDD    int `json:"ddd"`
	Number int `json:"number"`
}

var phones = [...]Phone{
	{DDD: 11, Number: 987654321},
	{DDD: 21, Number: 123456789},
	{DDD: 22, Number: 987654320},
	{DDD: 31, Number: 123456788},
	{DDD: 32, Number: 987654319},
	{DDD: 41, Number: 123456787},
	{DDD: 42, Number: 987654318},
	{DDD: 51, Number: 123456786},
	{DDD: 52, Number: 987654317},
	{DDD: 61, Number: 123456785},
	{DDD: 62, Number: 987654316},
	{DDD: 71, Number: 123456784},
	{DDD: 72, Number: 987654315},
	{DDD: 81, Number: 123456783},
	{DDD: 82, Number: 987654314},
	{DDD: 91, Number: 123456782},
	{DDD: 92, Number: 987654313},
	{DDD: 10, Number: 123456781},
	{DDD: 10, Number: 987654312},
}

// Function to handle GET requests for the list of telephones
func getPhonesHandler(w http.ResponseWriter, r *http.Request) {
	// clientID := r.PathValue("client_id")
	// fmt.Printf("Handling client id: %s\n", clientID)
	// Set content type as JSON
	w.Header().Set("Content-Type", "application/json")

	// Marshal the list of phones to JSON
	jsonBytes, err := json.Marshal(phones)
	if err != nil {
		fmt.Fprintf(w, "Error marshalling telephones to JSON: %v", err)
		return
	}

	// Write the JSON response
	w.Write(jsonBytes)
}

func main() {
	// Register the handler for the GET request
	http.HandleFunc("/address/{client_id}", getAddressHandler)
	http.HandleFunc("/phones/{client_id}", getPhonesHandler)

	// Start the server on port 8080
	fmt.Println("Server listening on port 8080")
	err := http.ListenAndServe(":8080", nil)
	if err != nil {
		panic(err)
	}
}
