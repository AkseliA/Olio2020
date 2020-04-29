package com.example.olio_ht;



public abstract class Account {
    double balance = 0;
    String accountNumber;
    String cardNumber;
    boolean card;
    boolean makePayments;

    public Account(){}
    public Account(String accountNumber, double amount, boolean card, boolean makePayments) {
        this.accountNumber = accountNumber;
        this.balance = amount;
        this.card = card;
        this.makePayments = makePayments;
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


    @Override
    public String toString() {
        return Account.class.getSimpleName() + " " + accountNumber;
    }
    public boolean isCard() {
        return card;
    }

    public void setCard(boolean card) {
        this.card = card;
    }

    public boolean isMakePayments() {
        return makePayments;
    }

    public void setMakePayments(boolean makePayments) {
        this.makePayments = makePayments;
    }
}
