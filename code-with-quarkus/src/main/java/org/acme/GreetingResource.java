package org.acme;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {
    private static AtomicInteger counter = new AtomicInteger(0);

    private static Client[] clients = new Client[] {
        new Client("A", 18),
        new Client("B", 19),
        new Client("C", 20),
        new Client("D", 21),
        new Client("E", 22),
        new Client("F", 23),
        new Client("G", 24),
        new Client("H", 25),
        new Client("I", 26),
        new Client("J", 27),
    };

    @Inject
    @RestClient
    MyRemoteService myRemoteService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public ResponseLoanOptions getLoanOptions() {
        int client_id = counter.getAndUpdate(value -> (value + 1) % 10);
        var client = clients[client_id];

        if (client.getAge() >= 18) {

        } else {
            // branch never taken
            // to test speculation performance

        }

        System.out.println(client_id);
        System.out.println(myRemoteService.getAddress(client_id));
        System.out.println(myRemoteService.getPhones(client_id));
        return new ResponseLoanOptions();
    }
}
