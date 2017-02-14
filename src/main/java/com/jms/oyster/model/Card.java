package com.jms.oyster.model;

public class Card {
    private final Integer number;
    private final String ownerName;
    private Double balance;

    public Card(Integer number, String ownerName, Double balance) {
        this.number = number;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public Integer getNumber() {
        return number;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Double getBalance() {
        return balance;
    }

    public Double addAmount(Double amount) {
        balance += amount;

        return balance;
    }

    public Double removeAmount(Double amount) {
        balance -= amount;

        return balance;
    }
}
