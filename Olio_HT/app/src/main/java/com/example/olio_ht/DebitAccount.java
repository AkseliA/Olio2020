package com.example.olio_ht;
/* The vault - android banking application
 *  Author: Akseli Aula 0545267
 *  Object Oriented programming course final project
 *  2020 */
public class DebitAccount extends Account {


    public DebitAccount(String accountNumber, double amount, String cardNumber, boolean makePayments) {
        super(accountNumber, amount, cardNumber, makePayments);
    }

    public DebitAccount() {
    }
}
