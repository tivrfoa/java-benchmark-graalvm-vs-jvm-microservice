package org.acme.reactive.crud;

import java.util.List;
import java.util.concurrent.CompletionStage;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;

public record Movie(String title, short year, double cost, String director) {

    public static Multi<Movie> findAll(PgPool pgPool) {
        return pgPool.query("select title, year, cost, director from movie").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Movie::from);
    }

    public static CompletionStage<List<Movie>> findAllAsList(PgPool pgPool) {
        return pgPool.query("select title, year, cost, director from movie").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Movie::from).collect().asList().subscribeAsCompletionStage();
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
