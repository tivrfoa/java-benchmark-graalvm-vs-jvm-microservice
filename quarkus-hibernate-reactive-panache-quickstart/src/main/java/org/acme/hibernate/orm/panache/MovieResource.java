package org.acme.hibernate.orm.panache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public Uni<ClientFavoriteDirectorMovies> getClientFavoriteDirectorMovies() {
        var client = Client.getClient();
        return Uni.combine()
            .all()
            .unis(Movie.findAll().list())
            .with(movies -> {
                HashMap<String, List<Movie>> moviesByDirector = new HashMap<>();
                System.out.println(movies);
                @SuppressWarnings("unchecked")
                var movieList = (List<Movie>) movies.get(0);
                for (var m : movieList) {
                    var movie = (Movie) m;
                    List<Movie> l = moviesByDirector.computeIfAbsent(movie.getDirector(), d -> new ArrayList<>());
                    l.add(movie);
                }
                return new ClientFavoriteDirectorMovies(client, moviesByDirector.get(client.getFavoriteDirector()));
            });
    }
}
