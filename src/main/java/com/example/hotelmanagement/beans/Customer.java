package com.example.hotelmanagement.beans;

public class Customer {
    private int customerId;
    private String fullName;
    private String cin;
    private String phone;
    private String email;
    private String password;
    private String address;
    private String account_status;

    public  Customer(){}
    public Customer(String fullName, String cin, String phone, String email, String password, String address, String account_status) {
        this.fullName = fullName;
        this.cin = cin;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.address = address;
        this.account_status = account_status;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int clientId) {
        this.customerId = clientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccount_status() {
        return account_status;
    }

    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", fullName='" + fullName + '\'' +
                ", cin='" + cin + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", account_status='" + account_status + '\'' +
                '}';
    }
}
