package org.acme;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hello")
public class GreetingResource {
    private static final List<LoanOption> NO_LOAN_OPTIONS_AVAILABLE = List.of();

    @Inject
    @RestClient
    MyRemoteService myRemoteService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoanOptions() {
        var client = Client.getClient();
        var phones = myRemoteService.getPhones(client.getId());
        client.setPhones(phones);

        if (client.getAge() >= 18) {
            var address = myRemoteService.getAddress(client.getId());
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

    @GET
    @Path("/NonBlocking")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getLoanOptionsNonBlocking() {
        var client = Client.getClient();
        var phonesFuture = myRemoteService.getPhonesAsync(client.getId());
        final Uni<Address> addressFuture;
        final List<LoanOption> loanOptions;
        if (client.getAge() >= 18) {
            addressFuture = myRemoteService.getAddressAsync(client.getId());
            loanOptions = calculateLoanOptions(client);
        } else {
            // branch never taken
            // to test speculative performance
            System.err.println("BUG: this should be unreachable");
            addressFuture = myRemoteService.getAddressAsync(client.getGuardianID());
            loanOptions = NO_LOAN_OPTIONS_AVAILABLE;
        }

        return Uni.combine().all().unis(phonesFuture, addressFuture)
            .with((phones, address) -> {
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
