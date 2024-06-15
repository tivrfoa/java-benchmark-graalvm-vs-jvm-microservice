package org.acme.hibernate.orm.panache;

public class Phone {

    private final int ddd;
    private final int number;

    public Phone(int ddd, int number) {
        this.ddd = ddd;
        this.number = number;
    }

    public int getDdd() {
        return ddd;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Phone [ddd=" + ddd + ", number=" + number + "]";
    }

}

