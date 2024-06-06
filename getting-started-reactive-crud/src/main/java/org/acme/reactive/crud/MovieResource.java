package org.acme.reactive.crud;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("db")
public class MovieResource {

    private final PgPool client;

    public MovieResource(PgPool client) {
        this.client = client;
    }

    @GET
    public Multi<Movie> get() {
        return Movie.findAll(client);
    }
}
