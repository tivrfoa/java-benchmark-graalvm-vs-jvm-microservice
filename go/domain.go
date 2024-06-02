package main

type Address struct {
	Street  string `json:"street"`
	Number  int    `json:"number"`
	State   string `json:"state"`
	Country string `json:"country"`
}

type Phone struct {
	DDD    int `json:"ddd"`
	Number int `json:"number"`
}

type Client struct {
	ID               uint32  `json:"id"`
	Name             string  `json:"name"`
	Age              uint32  `json:"age"`
	GuardianID       uint32  `json:"guardianID"`
	MonthSalary      float64 `json:"monthSalary"`
	Address          Address `json:"address"`
	Phones           []Phone `json:"phones"`
	FavoriteDirector string
}

type LoanOption struct {
	Years int `json:"years"`

	// Gemini suggested loanAmount, which is better! But let's keep the same as the Java version.
	Loan               float64 `json:"loan"`
	MonthlyInstallment float64 `json:"monthlyInstallment"`
}

type ResponseLoanOptions struct {
	Client      Client       `json:"client"`
	LoanOptions []LoanOption `json:"loanOptions"`
}

type Movie struct {
	Title    string  `pg:"title,notnull"`
	Year     int16   `pg:"year"`
	Cost     float64 `pg:"cost"`
	Director string  `pg:"director"`
}

type ClientFavoriteDirectorMovies struct {
	Client Client  `json:"client"`
	Movies []Movie `json:"movies"`
}
