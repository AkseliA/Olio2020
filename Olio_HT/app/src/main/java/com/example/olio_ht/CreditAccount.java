package com.example.olio_ht;

public class CreditAccount extends Account {
    private int limit;

    public CreditAccount(String accountNumber, double amount, int limit) {
        super(accountNumber, amount);
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}

