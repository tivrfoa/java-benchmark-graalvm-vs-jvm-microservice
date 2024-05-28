// Code generated from Gemini
// prompt: I need Go code to call those two Go endpoints that you created earlier.
// It will a different Go program that will call the address and phone endpoints
package main

import (
    "encoding/json"
    "fmt"
    "net/http"
)

// Define a struct to hold client information
type Client struct {
    ID       int      `json:"id"`
    Address  Address `json:"address"`
    Phones   []Phone `json:"phones"`
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
    DDD   int `json:"ddd"`
    Number int `json:"number"`
}

func main() {
    // Register the handler for the GET request
    http.HandleFunc("/hello", getHelloHandler)

    // Start the server on port 8080
    fmt.Println("Server listening on port 8081")
    err := http.ListenAndServe(":8081", nil)
    if err != nil {
        panic(err)
    }
}

func getHelloHandler(w http.ResponseWriter, r *http.Request) {
    // Set content type as JSON
    w.Header().Set("Content-Type", "application/json")

    client := CallAPI()

    // Marshal the Client struct to JSON
    clientJSON, err := json.Marshal(client)
    if err != nil {
        fmt.Println("Error marshalling client data:", err)
        return
    }

    // Write the JSON response
    w.Write(clientJSON)
}

func CallAPI() (*Client) {
    // Replace these with the actual URLs of your Go server endpoints
    addressURL := "http://localhost:8080/address"
    phonesURL := "http://localhost:8080/phones"

    // Replace this with the desired client ID
    // TODO atomic counter % 10
    clientID := 123

    // Function to fetch address data for a client
    getAddress := func() (*Address, error) {
        url := fmt.Sprintf("%s/%d", addressURL, clientID)  // Format URL with client ID
        req, err := http.NewRequest(http.MethodGet, url, nil)
        if err != nil {
            return nil, err
        }

        client := &http.Client{}
        resp, err := client.Do(req)
        if err != nil {
            return nil, err
        }
        defer resp.Body.Close()

        if resp.StatusCode != http.StatusOK {
            return nil, fmt.Errorf("unexpected status code: %d", resp.StatusCode)
        }

        var address Address
        err = json.NewDecoder(resp.Body).Decode(&address)
        if err != nil {
            return nil, err
        }

        return &address, nil
    }

    // Function to fetch phone numbers for a client
    getPhones := func() ([]Phone, error) {
        url := fmt.Sprintf("%s/%d", phonesURL, clientID)  // Format URL with client ID
        req, err := http.NewRequest(http.MethodGet, url, nil)
        if err != nil {
            return nil, err
        }

        client := &http.Client{}
        resp, err := client.Do(req)
        if err != nil {
            return nil, err
        }
        defer resp.Body.Close()

        if resp.StatusCode != http.StatusOK {
            return nil, fmt.Errorf("unexpected status code: %d", resp.StatusCode)
        }

        var phones []Phone
        err = json.NewDecoder(resp.Body).Decode(&phones)
        if err != nil {
            return nil, err
        }

        return phones, nil
    }

    // Call functions to retrieve client information
    address, err := getAddress()
    if err != nil {
        fmt.Println("Error fetching address:", err)
        return nil
    }

    phones, err := getPhones()
    if err != nil {
        fmt.Println("Error fetching phones:", err)
        return nil
    }

    // Create a Client struct with retrieved data
    client := Client{
        ID:       clientID,
        Address:  *address,  // Dereference address pointer
        Phones:   phones,
    }


    // fmt.Println("Client information:")
    // fmt.Println(string(clientJSON)) // Print JSON representation of client data
    return &client
}
