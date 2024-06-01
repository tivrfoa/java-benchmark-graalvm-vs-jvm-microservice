package org.acme;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hello")
public class GreetingResource {
    private static final List<LoanOption> NO_LOAN_OPTIONS_AVAILABLE = List.of();

    private static AtomicInteger counter = new AtomicInteger(0);

    private static Client[] clients = new Client[] {
        new Client("A", 18, 3000),
        new Client("B", 19, 3500),
        new Client("C", 20, 4000),
        new Client("D", 21, 4500),
        new Client("E", 22, 5000),
        new Client("F", 23, 6000),
        new Client("G", 24, 7000),
        new Client("H", 25, 8000),
        new Client("I", 26, 9000),
        new Client("J", 27, 10000),
    };

    @Inject
    @RestClient
    MyRemoteService myRemoteService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoanOptions() {
        int client_id = counter.getAndUpdate(value -> (value + 1) % 10);
        var client = clients[client_id];
        var phones = myRemoteService.getPhones(client_id);
        client.setPhones(phones);

        if (client.getAge() >= 18) {
            var address = myRemoteService.getAddress(client_id);
            client.setAddress(address);
            var loanOptions = calculateLoanOptions(client);
            return Response.ok(new ResponseLoanOptions(client, loanOptions)).build();
        } else {
            // branch never taken
            // to test speculative performance
            System.err.println("BUG: this should be unreachable");
            var address = myRemoteService.getAddress(client.getGuardianID());
            client.setAddress(address);
            return Response.ok(new ResponseLoanOptions(client, NO_LOAN_OPTIONS_AVAILABLE)).build();
        }
    }

    // @NonBlocking
    @GET
    @Path("/NonBlocking")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> getLoanOptionsNonBlocking() {
        int client_id = counter.getAndUpdate(value -> (value + 1) % 10);
        var client = clients[client_id];
        var phonesFuture = myRemoteService.getPhonesAsync(client_id);
        final CompletionStage<Address> addressFuture;
        final List<LoanOption> loanOptions;
        if (client.getAge() >= 18) {
            addressFuture = myRemoteService.getAddressAsync(client_id);
            loanOptions = calculateLoanOptions(client);
        } else {
            // branch never taken
            // to test speculative performance
            System.err.println("BUG: this should be unreachable");
            addressFuture = myRemoteService.getAddressAsync(client.getGuardianID());
            loanOptions = NO_LOAN_OPTIONS_AVAILABLE;
        }

        return addressFuture
            .thenCombine(phonesFuture, (address, phones) -> {
                client.setAddress(address);
                client.setPhones(phones);
                return Response.ok(new ResponseLoanOptions(client, loanOptions)).build();
            });
    }

    private List<LoanOption> calculateLoanOptions(Client client) {
        List<LoanOption> list = new ArrayList<>();
        for (int year = 1; year <= 3; year++) {
            double monthlyInstallment = 0.03 * client.getMonthSalary();
            double max = (monthlyInstallment * (12 * year));
            double interest = 0.07 * max;
            double loan = max - interest;
            list.add(new LoanOption(year, loan, monthlyInstallment));
        }
        return list;
    }
}
