package com.example.starter;

public class Phone {
    private int ddd;
    private int number;

    public Phone(int i, int j) {
      ddd = i;
      number = j;
    }

    public int getDdd() {
      return ddd;
    }

    public void setDdd(int ddd) {
      this.ddd = ddd;
    }

    public int getNumber() {
      return number;
    }

    public void setNumber(int number) {
      this.number = number;
    } 
}
