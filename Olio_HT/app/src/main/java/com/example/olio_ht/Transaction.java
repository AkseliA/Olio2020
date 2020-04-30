package com.example.olio_ht;

public class Transaction {
    private String from;
    private String to;
    private String date;
    private String amount;
    private String balance;
    private String accNmbr;

    public Transaction(String from, String to, String date, String amount, String balance, String accNmbr){
        this.from = from;
        this.to = to;
        this.date = date;
        this.amount = amount;
        this.balance = balance;
        this.accNmbr = accNmbr;

    }
    public Transaction(){}
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
