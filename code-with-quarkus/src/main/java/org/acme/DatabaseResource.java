package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/db")
public class DatabaseResource {

    @Inject
    DatabaseService databaseService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovies() {
        return Response.ok(databaseService.getClientFavoriteDirectorMovies()).build();
    }
}
