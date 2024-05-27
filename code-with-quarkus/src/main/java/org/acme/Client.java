package org.acme;

public class Client {

    private String name;
    private int age;
    private Address address;
    private Phone phone;

    public Client(String name, int age) {
        this.name = name;
        this.age = age;
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
    public Phone getPhone() {
        return phone;
    }
    public void setPhone(Phone phone) {
        this.phone = phone;
    }
    @Override
    public String toString() {
        return "Client [name=" + name + ", age=" + age + ", address=" + address + ", phone=" + phone + "]";
    }

}
