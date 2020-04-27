package com.example.olio_ht;

public class SavingsAccount extends Account {
    private double interest;

    public SavingsAccount(String accountNumber, double amount) {
        super(accountNumber, amount);
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
}
