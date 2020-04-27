package com.example.olio_ht;

public class User {
    private String first_name;
    private String last_name;
    private String email;
    private String address;
    private String phone;
    private String credit_account;
    private String debit_account;
    private String savings_account;

    public User() {
    }

    public User(String first_name, String last_name, String email, String address, String phone, String credit_account, String debit_account, String savings_account) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.credit_account = credit_account;
        this.debit_account = debit_account;
        this.savings_account = savings_account;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCredit_account() {
        return credit_account;
    }

    public void setCredit_account(String credit_account) {
        this.credit_account = credit_account;
    }

    public String getDebit_account() {
        return debit_account;
    }

    public void setDebit_account(String debit_account) {
        this.debit_account = debit_account;
    }

    public String getSavings_account() {
        return savings_account;
    }

    public void setSavings_account(String savings_account) {
        this.savings_account = savings_account;
    }

}
