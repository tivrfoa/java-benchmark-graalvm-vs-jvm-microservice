package org.acme;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(baseUri = "http://localhost:8080")
public interface MyRemoteService {

    @GET
    @Path("/address")
    @Produces(MediaType.APPLICATION_JSON)
    String getAddress();

    @GET
    @Path("/phones")
    @Produces(MediaType.APPLICATION_JSON)
    String getPhones();
}
