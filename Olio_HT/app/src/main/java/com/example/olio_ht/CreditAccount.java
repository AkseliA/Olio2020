package com.example.olio_ht;

public class CreditAccount extends Account {
    private int limit;

    public CreditAccount(String accountNumber, double amount, boolean card, boolean makePayments, int limit) {
        super(accountNumber, amount, card, makePayments);
        this.limit = limit;
    }

    public CreditAccount(){}

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void setBalance(double amount) {
        if(balance + amount > 0) {
            balance = balance + amount;
        }else {
            System.out.println("You don't have enough money.");
        }
    }

}

