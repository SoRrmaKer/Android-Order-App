package com.example.ordersystem.bean;

import java.io.Serializable;

public class ReviewBean implements Serializable {
    private String reviewId;
    private String orderId;
    private String userId;
    private String merchantId;
    private float rating;
    private String content;
    private String imgPath;
    private String time;
    private String userName;
    private String userAvatar;

    public ReviewBean() {
    }
    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }
    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImgPath() { return imgPath; }
    public void setImgPath(String imgPath) { this.imgPath = imgPath; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserAvatar() { return userAvatar; }
    public void setUserAvatar(String userAvatar) { this.userAvatar = userAvatar; }
}