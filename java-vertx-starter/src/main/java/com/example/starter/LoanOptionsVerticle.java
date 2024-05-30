package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LoanOptionsVerticle extends AbstractVerticle {

  private static final List<LoanOption> NO_LOAN_OPTIONS_AVAILABLE = List.of();
  private static final List<Phone> ONE_PHONE = List.of(new Phone(61, 99999999));
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

  // public static void main(String[] args) {
  //   Vertx vertx = Vertx.vertx();
  //   vertx.deployVerticle(LoanOptionsVerticle.class.getName());
  // }
  WebClient webClient;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.get("/hello").handler(this::getServiceHandler);
    WebClientOptions webClientOptions = new WebClientOptions();
    webClientOptions.setMaxPoolSize(40);
    webClient = WebClient.create(vertx, webClientOptions);

    vertx.createHttpServer()
        .requestHandler(router)
        .listen(8081)
        .onSuccess(server -> {
          startPromise.complete();
          System.out.println("HTTP server started on port " + server.actualPort());
        })
        .onFailure(err -> err.printStackTrace());
  }

  private void getServiceHandler(io.vertx.ext.web.RoutingContext context) {
    int clientID = counter.getAndUpdate(value -> (value + 1) % 10);
    var client = clients[clientID];

    // var webClient = WebClient.create(vertx);
    var phonesFuture = webClient
        .get(8080, "127.0.0.1", "http://127.0.0.1:8080/phones/" + clientID)
        .send()
        .onSuccess(resp -> {
          // client.setPhones(resp.bodyAsJson(List.class));
          client.setPhones(ONE_PHONE);
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

}
