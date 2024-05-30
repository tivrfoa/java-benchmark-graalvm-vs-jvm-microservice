package com.example.starter;

import java.util.List;

public class Client {
    private String name;
    private int age;
    private double monthSalary;
    private int guardianID;
    private Address address;
    private List<Phone> phones;

    public Client(String name, int age, double monthSalary) {
      this.name = name;
      this.age = age;
      this.monthSalary = monthSalary;
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

    public double getMonthSalary() {
      return monthSalary;
    }

    public void setMonthSalary(double monthSalary) {
      this.monthSalary = monthSalary;
    }

    public int getGuardianID() {
      return guardianID;
    }

    public void setGuardianID(int guardianID) {
      this.guardianID = guardianID;
    }

    public Address getAddress() {
      return address;
    }

    public void setAddress(Address address) {
      this.address = address;
    }

    public List<Phone> getPhones() {
      return phones;
    }

    public void setPhones(List<Phone> phones) {
      this.phones = phones;
    }
  }
