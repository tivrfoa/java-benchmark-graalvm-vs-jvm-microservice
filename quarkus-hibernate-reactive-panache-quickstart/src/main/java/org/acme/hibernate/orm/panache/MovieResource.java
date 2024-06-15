package org.acme.hibernate.orm.panache;

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
    public Uni<List<Movie>> get() {
        return Movie.listAll(Sort.by("title"));
    }

    @GET
    @Path("{id}")
    public Uni<Fruit> getSingle(Long id) {
        return Fruit.findById(id);
    }
}
