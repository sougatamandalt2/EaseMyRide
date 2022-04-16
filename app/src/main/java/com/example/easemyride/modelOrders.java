package com.example.easemyride;

public class modelOrders {

    String id,pid,name,rate,totalCost,quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public modelOrders() {
    }

    public modelOrders(String id, String pid, String name, String rate, String totalCost, String quantity) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.rate = rate;
        this.totalCost = totalCost;
        this.quantity = quantity;
    }
}
