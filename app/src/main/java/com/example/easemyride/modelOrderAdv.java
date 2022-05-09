package com.example.easemyride;

public class modelOrderAdv {

    String longitude,latitude,orderTo,orderBy,orderCost,orderTitle,orderStatus,orderTime,orderID,orderDate;

    public modelOrderAdv() {
    }

    public modelOrderAdv(String longitude, String latitude, String orderTo, String orderBy, String orderCost, String orderTitle, String orderStatus, String orderTime, String orderID, String orderDate) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.orderTo = orderTo;
        this.orderBy = orderBy;
        this.orderCost = orderCost;
        this.orderTitle = orderTitle;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.orderID = orderID;
        this.orderDate = orderDate;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getOrderTo() {
        return orderTo;
    }

    public void setOrderTo(String orderTo) {
        this.orderTo = orderTo;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(String orderCost) {
        this.orderCost = orderCost;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
