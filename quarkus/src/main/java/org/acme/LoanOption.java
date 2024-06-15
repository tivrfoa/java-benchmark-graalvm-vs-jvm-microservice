package org.acme;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class LoanOption {
    private int years;
    private double loan;
    private double monthlyInstallment;
    
    public LoanOption(int years, double loan, double monthlyInstallment) {
        this.years = years;
        this.loan = loan;
        this.monthlyInstallment = monthlyInstallment;
    }
    public int getYears() {
        return years;
    }
    public void setYears(int years) {
        this.years = years;
    }
    public double getLoan() {
        return loan;
    }
    public void setLoan(double loan) {
        this.loan = loan;
    }
    public double getMonthlyInstallment() {
        return monthlyInstallment;
    }
    public void setMonthlyInstallment(double monthlyInstallment) {
        this.monthlyInstallment = monthlyInstallment;
    }
    @Override
    public String toString() {
        return "LoanOption [years=" + years + ", loan=" + loan + ", monthlyInstallment=" + monthlyInstallment + "]";
    }

    
}
