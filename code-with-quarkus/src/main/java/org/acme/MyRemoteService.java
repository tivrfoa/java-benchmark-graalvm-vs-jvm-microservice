package org.acme;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(baseUri = "http://localhost:8080")
public interface MyRemoteService {

    @GET
    @Path("/address/{client_id}")
    @Produces(MediaType.APPLICATION_JSON)
    Address getAddress(@PathParam("client_id") int clientID);

    @GET
    @Path("/phones/{client_id}")
    @Produces(MediaType.APPLICATION_JSON)
    List<Phone> getPhones(@PathParam("client_id") int clientID);
}
