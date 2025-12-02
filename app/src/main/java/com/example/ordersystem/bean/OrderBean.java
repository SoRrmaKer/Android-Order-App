package com.example.ordersystem.bean;

import java.io.Serializable;
import java.util.List;

public class OrderBean implements Serializable {

    private String orderId;
    private String orderTime;
    private String merchantId;
    private String userId;
    private String address;
    private int status; // 0: Pending, 1: Completed, 2: Cancelled
    private String totalPrice;
    private String userName;
    private String userPhone;
    private String userAvatar;
    private String merchantName;
    private String merchantAvatar;
    private boolean isReviewed;

    private List<OrderDetailBean> detailsList;

    public OrderBean() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public List<OrderDetailBean> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<OrderDetailBean> detailsList) {
        this.detailsList = detailsList;
    }

    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }

    public String getMerchantAvatar() { return merchantAvatar; }
    public void setMerchantAvatar(String merchantAvatar) { this.merchantAvatar = merchantAvatar; }
    public boolean isReviewed() { return isReviewed; }
    public void setReviewed(boolean reviewed) { isReviewed = reviewed; }
}