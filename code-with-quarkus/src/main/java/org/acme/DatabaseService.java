package org.acme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class DatabaseService {
    @Inject
    AgroalDataSource movieDataSource;

    public ClientFavoriteDirectorMovies getClientFavoriteDirectorMovies() {
        var client = Client.getClient();
        List<Movie> movies = new ArrayList<>();
        try (var conn = movieDataSource.getConnection();
             var stmt = conn.createStatement();
             var resultSet = stmt.executeQuery("select title, year, cost, director from movie")) {
            while (resultSet.next()) {
                movies.add(new Movie(
                    resultSet.getString("title"),
                    resultSet.getShort("year"),
                    resultSet.getDouble("cost"),
                    resultSet.getString("director")
                ));
            }

            HashMap<String, List<Movie>> moviesByDirector = new HashMap<>();
            for (var movie : movies) {
                List<Movie> l = moviesByDirector.computeIfAbsent(movie.director(), d -> new ArrayList<>());
	            l.add(movie);
            }
            return new ClientFavoriteDirectorMovies(client, moviesByDirector.get(client.getFavoriteDirector()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ClientFavoriteDirectorMovies(null, null);
        }
    }
}
