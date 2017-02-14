package com.jms.oyster.model;

import com.jms.oyster.exception.IllegalParameterException;

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

    public Double addAmount(Double amount) throws IllegalParameterException {
        if(amount < 0.0D) {
            throw new IllegalParameterException("Cannot add a negative amount of money to a card");
        }
        balance += amount;

        return balance;
    }

    public Double removeAmount(Double amount) throws IllegalParameterException {
        if(amount < 0.0D) {
            throw new IllegalParameterException("Cannot remove a negative amount of money from a card");
        }

        balance -= amount;

        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (!number.equals(card.number)) return false;
        if (!ownerName.equals(card.ownerName)) return false;
        return balance.equals(card.balance);

    }

    @Override
    public int hashCode() {
        int result = number.hashCode();
        result = 31 * result + ownerName.hashCode();
        result = 31 * result + balance.hashCode();
        return result;
    }
}
