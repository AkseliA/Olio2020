package com.example.olio_ht;

public class CreditAccount extends Account {
    private int limit;

    public CreditAccount(String accountNumber, double amount, int limit) {
        super(accountNumber, amount);
        this.limit = limit;
    }
    public int getLimit() {
        return limit;
    }

    @Override
    public void setBalance(double amount){
        if((balance + amount) >= -limit){
            balance += amount;
        }else{
            //Credit limit exceeded
        }
    }
}
