package com.example.olio_ht;

public abstract class Account {
    double balance = 0;
    String accountNumber;

    public Account(){}
    public Account(String accountNumber, double amount) {
        this.accountNumber = accountNumber;
        this.balance = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double amount) {
        this.balance = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
