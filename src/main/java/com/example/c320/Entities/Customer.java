package com.example.c320.Entities;

import jakarta.persistence.*;

@Entity
public class Customer {

    @Id
    private long Id;

    private double balance;

    private String email;

    private String name;


    public Customer(long id, double balance, String email, String name) {
        Id = id;
        this.balance = balance;
        this.email = email;
        this.name = name;
    }


    public Customer() {

    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
