package org.acme.hibernate.orm.panache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionStage;

import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("db")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class MovieResource {

    @GET
    @Path("/movies")
    public Uni<List<Movie>> get() {
        return Movie.listAll(Sort.by("title"));
    }

    @GET
    public CompletionStage<ClientFavoriteDirectorMovies> getClientFavoriteDirectorMovies() {
        var client = Client.getClient();
        return Movie.findAll()
            .list()
            .subscribeAsCompletionStage()
            .thenApplyAsync(movies -> {
                HashMap<String, List<Movie>> moviesByDirector = new HashMap<>();
                for (var m : movies) {
                    var movie = (Movie) m;
                    List<Movie> l = moviesByDirector.computeIfAbsent(movie.getDirector(), d -> new ArrayList<>());
                    l.add(movie);
                }
                return new ClientFavoriteDirectorMovies(client, moviesByDirector.get(client.getFavoriteDirector()));
            });
    }
}
