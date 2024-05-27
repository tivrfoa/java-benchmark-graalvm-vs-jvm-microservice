package org.acme;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ResponseLoanOptions {

    private Client client;
    private List<LoanOption> loanOptions;
    
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public List<LoanOption> getLoanOptions() {
        return loanOptions;
    }
    public void setLoanOptions(List<LoanOption> loanOptions) {
        this.loanOptions = loanOptions;
    }
    public ResponseLoanOptions(Client client, List<LoanOption> loanOptions) {
        this.client = client;
        this.loanOptions = loanOptions;
    }
    
}
