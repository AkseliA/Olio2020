package com.example.olio_ht;
/* The vault - android banking application
 *  Author: Akseli Aula 0545267
 *  Object Oriented programming course final project
 *  2020 */

public abstract class Account {
    double balance = 0;
    String accountNumber;
    String cardNumber;
    boolean makePayments;

    public Account() {
    }

    public Account(String accountNumber, double amount, String cardNumber, boolean makePayments) {
        this.accountNumber = accountNumber;
        this.balance = amount;
        this.cardNumber = cardNumber;
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


    public boolean isMakePayments() {
        return makePayments;
    }

    public void setMakePayments(boolean makePayments) {
        this.makePayments = makePayments;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
