package com.example.olio_ht;

public class SavingsAccount extends Account {
    private double interest;

    public SavingsAccount(String accountNumber, double amount, String cardNumber, boolean makePayments, double interest) {
        super(accountNumber, amount, cardNumber, makePayments);
        this.interest = interest;
    }
    public SavingsAccount(){}

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
}
