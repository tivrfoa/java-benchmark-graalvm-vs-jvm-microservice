package org.acme;

import java.util.ArrayList;
import java.util.List;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class DatabaseService {
    @Inject
    AgroalDataSource movieDataSource;

    public void printMovies() {
        List<Movie> movies = new ArrayList<>();
        try {
            var conn = movieDataSource.getConnection();
            var stmt = conn.createStatement();
            var resultSet = stmt.executeQuery("select title, year, cost, director from movie");
            while (resultSet.next()) {
                movies.add(new Movie(
                    resultSet.getString("title"),
                    resultSet.getShort("year"),
                    resultSet.getDouble("cost"),
                    resultSet.getString("director")
                ));
            }
            System.out.println(movies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
