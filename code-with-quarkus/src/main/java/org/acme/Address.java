package org.acme;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Address {

    private final String street;
    private final int number;
    private final String state;
    private final String country;

    public Address(String street, int number, String state, String country) {
        this.street = street;
        this.number = number;
        this.state = state;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public int getNumber() {
        return number;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Address [street=" + street + ", number=" + number + ", state=" + state + ", country=" + country + "]";
    }

}
