package com.example.olio_ht;
/* The vault - android banking application
 *  Author: Akseli Aula 0545267
 *  Object Oriented programming course final project
 *  2020 */
public class Transaction {
    private String action;
    private String date;
    private String amount;
    private String balance;
    private String accNmbr;

    public Transaction(String action, String date, String amount, String balance, String accNmbr) {
        this.action = action;
        this.date = date;
        this.amount = amount;
        this.balance = balance;
        this.accNmbr = accNmbr;

    }

    public Transaction() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAccNmbr() {
        return accNmbr;
    }

    public void setAccNmbr(String accNmbr) {
        this.accNmbr = accNmbr;
    }

}
