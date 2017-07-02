package com.project.gaurs.tadqa.Pojo;

/**
 * Created by gaurs on 6/16/2017.
 */

public class OrderElements {
    public OrderElements() {
    }

    String foodname ,url, price, rate, qty, name, address, transactionID;

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public OrderElements(String foodname, String url, String price, String rate, String qty, String name, String address, String transactionID) {

        this.foodname = foodname;
        this.url = url;
        this.price = price;
        this.rate = rate;
        this.qty = qty;
        this.name = name;
        this.address = address;
        this.transactionID = transactionID;
    }
}
