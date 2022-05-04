package com.example.easemyride;

public class modelOrders {

    String id,pid,name,regNo,totalCost;

    public modelOrders() {
    }

    public modelOrders(String id, String pid, String name, String regNo, String totalCost) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.regNo = regNo;
        this.totalCost = totalCost;
    }

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

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }
}
