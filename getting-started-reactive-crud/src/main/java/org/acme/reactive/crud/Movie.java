package org.acme.reactive.crud;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;

public record Movie(String title, short year, double cost, String director) {

    public static Multi<Movie> findAll(PgPool client) {
        return client.query("select title, year, cost, director from movie").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Movie::from);
    }

    private static Movie from(Row row) {
        return new Movie(
            row.getString("title"),
            row.getShort("year"),
            row.getDouble("cost"),
            row.getString("director")
        );
    }
}
