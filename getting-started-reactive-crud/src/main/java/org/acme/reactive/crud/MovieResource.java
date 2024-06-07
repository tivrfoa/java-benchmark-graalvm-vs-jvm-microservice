package org.acme.reactive.crud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionStage;

import io.vertx.mutiny.pgclient.PgPool;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("db")
public class MovieResource {

    private final PgPool pgPool;

    public MovieResource(PgPool pgPool) {
        this.pgPool = pgPool;
    }

    @GET
    public CompletionStage<ClientFavoriteDirectorMovies> get() {
        final var client = Client.getClient();
        return Movie.findAll(pgPool)
            .collect()
            .asList()
            .subscribeAsCompletionStage()
            .thenApplyAsync(movies -> {
                HashMap<String, List<Movie>> moviesByDirector = new HashMap<>();
                for (var movie : movies) {
                    List<Movie> l = moviesByDirector.computeIfAbsent(movie.director(), d -> new ArrayList<>());
                    l.add(movie);
                }
                return new ClientFavoriteDirectorMovies(client, moviesByDirector.get(client.getFavoriteDirector()));
            });
    }
}
