package com.example.starter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {

    private int id;
    private String name;
    private int age;
    private int guardianID;
    private double monthSalary;
    private Address address;
    private List<Phone> phones;
    private String favoriteDirector;

    public Client(int id, String name, int age, double monthSalary, String favoriteDirector) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.monthSalary = monthSalary;
        this.favoriteDirector = favoriteDirector;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFavoriteDirector() {
        return favoriteDirector;
    }

    public void setFavoriteDirector(String favoriteDirector) {
        this.favoriteDirector = favoriteDirector;
    }

    @Override
    public String toString() {
        return "Client [name=" + name + ", age=" + age + ", guardianID=" + guardianID + ", monthSalary=" + monthSalary
                + ", address=" + address + ", phones=" + phones + "]";
    }

    private static AtomicInteger counter = new AtomicInteger(0);

    public static Client getClient() {
        int client_id = counter.getAndUpdate(value -> (value + 1) % 10);

        return switch(client_id) {
            case 0-> new Client(0, "A", 18, 3000, "J J Abrams");
            case 1-> new Client(1, "B", 19, 3500, "Louis Leterrier");
            case 2-> new Client(2, "C", 20, 4000, "Rob Marshall");
            case 3-> new Client(3, "D", 21, 4500, "Joss Whedon");
            case 4-> new Client(4, "E", 22, 5000, "Anthony Russo & Joe Russo");
            case 5-> new Client(5, "F", 23, 6000, "Sam Raimi");
            case 6-> new Client(6, "G", 24, 7000, "James Mangold");
            case 7-> new Client(7, "H", 25, 8000, "Gore Verbinski");
            case 8-> new Client(8, "I", 26, 9000, "Zack Snyder & Joss Whedon");
            case 9-> new Client(9, "J", 27, 10000, "Rian Johnson");
            default -> throw new RuntimeException("client_id should be from 0 to 9, got: " + client_id);
        };
    }
}