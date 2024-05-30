package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LoanOptionsVerticle extends AbstractVerticle {

  private static final List<LoanOption> NO_LOAN_OPTIONS_AVAILABLE = List.of();
  private static final AtomicInteger counter = new AtomicInteger(0);
  private static Client[] clients = new Client[] {
      new Client("A", 18, 3000),
      new Client("B", 19, 3500),
      new Client("C", 20, 4000),
      new Client("D", 21, 4500),
      new Client("E", 22, 5000),
      new Client("F", 23, 6000),
      new Client("G", 24, 7000),
      new Client("H", 25, 8000),
      new Client("I", 26, 8080),
      new Client("J", 27, 10000),
  };

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(LoanOptionsVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);
    router.get("/hello").handler(this::getServiceHandler);

    vertx.createHttpServer()
        .requestHandler(router)
        .listen(8081)
        .onSuccess(server -> System.out.println(
            "HTTP server started on port " + server.actualPort()))
        .onFailure(err -> err.printStackTrace());
  }

  private void getServiceHandler(io.vertx.ext.web.RoutingContext context) {
    int clientID = counter.getAndUpdate(value -> (value + 1) % 10);
    var client = clients[clientID];

    var webClient = WebClient.create(vertx);
    var phonesFuture = webClient
        .get(8080, "127.0.0.1", "http://127.0.0.1:8080/phones/" + clientID)
        .send()
        .onSuccess(resp -> {
          client.setPhones(resp.bodyAsJson(List.class));
        })
        .onFailure(err -> {
          System.err.println("Failed to get phones");
          err.printStackTrace();
        });

    Future<HttpResponse<Buffer>> addressFuture;
    final List<LoanOption> loanOptions;
    if (client.getAge() >= 18) {
      addressFuture = webClient.get(8080, "127.0.0.1", "http://127.0.0.1:8080/address/" + clientID).send();
      loanOptions = calculateLoanOptions(client);
    } else {
      addressFuture = webClient.get(8080, "127.0.0.1", "http://127.0.0.1:8080/address/" + client.getGuardianID())
          .send();
      loanOptions = NO_LOAN_OPTIONS_AVAILABLE;
    }

    addressFuture
        .onSuccess(resp -> client.setAddress(resp.bodyAsJson(Address.class)))
        .onFailure(err -> System.err.println(err.getMessage()));

    Future.all(phonesFuture, addressFuture)
        .onComplete(_h -> {
          JsonObject response = new JsonObject()
              .put("client", JsonObject.mapFrom(client))
              .put("loanOptions", JsonArray.of(loanOptions));
          context.response().putHeader("Content-Type", "application/json").end(response.encode());
        });
  }

  private List<LoanOption> calculateLoanOptions(Client client) {
    List<LoanOption> options = new ArrayList<>();
    for (int year = 1; year <= 3; year++) {
      double monthlyInstallment = client.getMonthSalary() * 0.03;
      double maxLoan = monthlyInstallment * 12 * year;
      double interest = maxLoan * 0.07;
      double loanAmount = maxLoan - interest;
      options.add(new LoanOption(year, loanAmount, monthlyInstallment));
    }
    return options;
  }

  public static class Client {
    private String name;
    private int age;
    private double monthSalary;
    private int guardianID;
    private Address address;
    private List<Phone> phones;

    public Client(String name, int age, double monthSalary) {
      this.name = name;
      this.age = age;
      this.monthSalary = monthSalary;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    public double getMonthSalary() {
      return monthSalary;
    }

    public void setMonthSalary(double monthSalary) {
      this.monthSalary = monthSalary;
    }

    public int getGuardianID() {
      return guardianID;
    }

    public void setGuardianID(int guardianID) {
      this.guardianID = guardianID;
    }

    public Address getAddress() {
      return address;
    }

    public void setAddress(Address address) {
      this.address = address;
    }

    public List<Phone> getPhones() {
      return phones;
    }

    public void setPhones(List<Phone> phones) {
      this.phones = phones;
    }
  }

  public static class Address {
    private String street;
    private int number;
    private String state;
    private String country;

    public String getStreet() {
      return street;
    }

    public void setStreet(String street) {
      this.street = street;
    }

    public int getNumber() {
      return number;
    }

    public void setNumber(int number) {
      this.number = number;
    }

    public String getState() {
      return state;
    }

    public void setState(String state) {
      this.state = state;
    }

    public String getCountry() {
      return country;
    }

    public void setCountry(String country) {
      this.country = country;
    }

  }

  public static class Phone {
    private int ddd;
    private int number;

    public int getDdd() {
      return ddd;
    }

    public void setDdd(int ddd) {
      this.ddd = ddd;
    }

    public int getNumber() {
      return number;
    }

    public void setNumber(int number) {
      this.number = number;
    }
  }

  public static class LoanOption {
    private int years;
    private double loan;
    private double monthlyInstallment;

    public LoanOption(int years, double loan, double monthlyInstallment) {
      this.years = years;
      this.loan = loan;
      this.monthlyInstallment = monthlyInstallment;
    }

    public int getYears() {
      return years;
    }

    public void setYears(int years) {
      this.years = years;
    }

    public double getLoan() {
      return loan;
    }

    public void setLoan(double loan) {
      this.loan = loan;
    }

    public double getMonthlyInstallment() {
      return monthlyInstallment;
    }

    public void setMonthlyInstallment(double monthlyInstallment) {
      this.monthlyInstallment = monthlyInstallment;
    }

  }
}
