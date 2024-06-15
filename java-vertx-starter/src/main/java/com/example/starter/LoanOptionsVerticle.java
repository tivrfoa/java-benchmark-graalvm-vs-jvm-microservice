package com.example.starter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgConnection;
import io.vertx.sqlclient.PreparedQuery;
import io.vertx.sqlclient.PreparedStatement;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowIterator;
import io.vertx.sqlclient.RowSet;

public class LoanOptionsVerticle extends AbstractVerticle {

	private static final List<LoanOption> NO_LOAN_OPTIONS_AVAILABLE = List.of();
	private static final String SELECT_WORLD = "select title, year, cost, director from movie";

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(LoanOptionsVerticle.class.getName());
	}

	private WebClient webClient;

	private PreparedQuery<RowSet<Row>> SELECT_WORLD_QUERY;
	private HttpServer httpServer;

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		Router router = Router.router(vertx);
		router.get("/hello").handler(this::getServiceHandler);
		router.get("/db").handler(this::handleDB);
		webClient = WebClient.create(vertx);

		httpServer = vertx.createHttpServer()
				.requestHandler(router);
		// .listen(8081)
		// .onSuccess(server -> {
		// startPromise.complete();
		// System.out.println("HTTP server started on port " + server.actualPort());
		// })
		// .onFailure(err -> err.printStackTrace());

		// https://github.com/TechEmpower/FrameworkBenchmarks/blob/master/frameworks/Java/vertx/src/main/java/vertx/App.java#L100
		JsonObject config = config();
		PgConnectOptions options = new PgConnectOptions();
		options.setDatabase(config.getString("database", "bench"));
		options.setHost(config.getString("host", "localhost"));
		options.setPort(config.getInteger("port", 5432));
		options.setUser(config.getString("username", "admin"));
		options.setPassword(config.getString("password", "123"));
		options.setCachePreparedStatements(true);
		options.setPipeliningLimit(100_000);
		Future<PgConnection> futurePgConnection = PgConnection.connect(vertx, options);
		Future<PreparedStatement> futurePreparedStatement = futurePgConnection.flatMap(conn -> {
			Future<PreparedStatement> f1 = conn.prepare(SELECT_WORLD)
					.andThen(onSuccess(ps -> SELECT_WORLD_QUERY = ps.query()));
			return f1;
		});
		futurePreparedStatement.transform(ar -> {
			return httpServer.listen(8081);
		})
				.<Void>mapEmpty()
				.onComplete(startPromise);
	}

	private static <T> Handler<AsyncResult<T>> onSuccess(Handler<T> handler) {
		return ar -> {
			if (ar.succeeded()) {
				handler.handle(ar.result());
			}
		};
	}

	private void handleDB(io.vertx.ext.web.RoutingContext context) {
		var client = Client.getClient();

		HttpServerResponse resp = context.response();
		SELECT_WORLD_QUERY.execute(res -> {
			if (res.succeeded()) {
				RowIterator<Row> resultSet = res.result().iterator();
				if (!resultSet.hasNext()) {
					System.err.println("opss");
					resp.setStatusCode(404).end();
					return;
				}
				List<Movie> movies = new ArrayList<>();
				resultSet.forEachRemaining(row -> {
					movies.add(new Movie(
							row.getString("title"),
							row.getShort("year"),
							row.getDouble("cost"),
							row.getString("director")));
				});

				HashMap<String, List<Movie>> moviesByDirector = new HashMap<>();
				for (var movie : movies) {
					List<Movie> l = moviesByDirector.computeIfAbsent(movie.director(), d -> new ArrayList<>());
					l.add(movie);
				}
				var clientFavoriteDirectorMovies = new ClientFavoriteDirectorMovies(client,
						moviesByDirector.get(client.getFavoriteDirector()));
				resp
						.putHeader("Content-Type", "application/json")
						.end(Json.encode(clientFavoriteDirectorMovies));
			} else {
				throw new RuntimeException(res.cause());
			}
		});
	}

	private void getServiceHandler(io.vertx.ext.web.RoutingContext context) {
		var client = Client.getClient();

		var phonesFuture = webClient
				.get(8080, "127.0.0.1", "http://127.0.0.1:8080/phones/" + client.getId())
				.send()
				.onSuccess(resp -> {
					client.setPhones(resp.bodyAsJson(List.class));
				})
				.onFailure(err -> {
					System.err.println("Failed to get phones");
					err.printStackTrace();
				});

		final Future<HttpResponse<Buffer>> addressFuture;
		final List<LoanOption> loanOptions;
		if (client.getAge() >= 18) {
			addressFuture = webClient.get(8080, "127.0.0.1", "http://127.0.0.1:8080/address/" + client.getId()).send();
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
