package org.acme;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Client {

    private String name;
    private int age;
    private int guardianID;
    private double monthSalary;
    private Address address;
    private List<Phone> phones;

    public Client(String name, int age, double monthSalary) {
        this.name = name;
        this.age = age;
        this.monthSalary = monthSalary;
    }

    public int getGuardianID() {
        return guardianID;
    }
    public void setGuardianID(int guardianID) {
        this.guardianID = guardianID;
    }

    public List<Phone> getPhones() {
        return phones;
    }
    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public double getMonthSalary() {
        return monthSalary;
    }
    public void setMonthSalary(double monthSalary) {
        this.monthSalary = monthSalary;
    }
    @Override
    public String toString() {
        return "Client [name=" + name + ", age=" + age + ", guardianID=" + guardianID + ", monthSalary=" + monthSalary
                + ", address=" + address + ", phones=" + phones + "]";
    }
    
}
