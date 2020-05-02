package com.example.olio_ht;

public class DebitAccount extends Account {


    public DebitAccount(String accountNumber, double amount, String cardNumber, boolean makePayments) {
        super(accountNumber, amount, cardNumber, makePayments);
    }
    public DebitAccount(){}
}
