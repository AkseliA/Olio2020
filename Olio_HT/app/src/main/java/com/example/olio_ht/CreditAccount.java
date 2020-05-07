package com.example.olio_ht;
/* The vault - android banking application
 *  Author: Akseli Aula 0545267
 *  Object Oriented programming course final project
 *  2020 */
public class CreditAccount extends Account {
    private int limit;

    public CreditAccount(String accountNumber, double amount, String cardNumber, boolean makePayments, int limit) {
        super(accountNumber, amount, cardNumber, makePayments);
        this.limit = limit;
    }

    public CreditAccount() {
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void setBalance(double amount) {
        if (balance + amount >= -limit) {
            balance = balance + amount;
        }
    }
}

