package org.acme;

import java.util.List;

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
    
}
