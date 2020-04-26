package com.example.olio_ht;

public abstract class Account {
    double balance = 0;
    String accountNumber;

    public Account(String accountNumber, double amount) {
        this.accountNumber = accountNumber;
        this.balance = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double amount) {
        if(balance + amount > 0){
            balance = balance + amount;
        }
        else{
            //Not enough money.
        }

    }

    public String getAccountNumber() {
        return accountNumber;
    }


}
