package com.example.olio_ht;

public class DebitAccount extends Account {


    public DebitAccount(String accountNumber, double amount, boolean card, boolean makePayments) {
        super(accountNumber, amount, card, makePayments);
    }
    public DebitAccount(){}
}
