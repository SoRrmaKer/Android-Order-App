package com.example.ordersystem.bean;

import java.io.Serializable;

public class FoodBean implements Serializable {

    @Override
    public String toString() {
        return "FoodBean{" +
                "foodID='" + foodID + '\'' +
                ", merchantID='" + merchantID + '\'' +
                ", foodName='" + foodName + '\'' +
                ", foodDes='" + foodDes + '\'' +
                ", foodPrice='" + foodPrice + '\'' +
                ", foodImg='" + foodImg + '\'' +
                '}';
    }

    public FoodBean(String foodID, String merchantID, String foodName, String foodDes, String foodPrice, String foodImg) {
        this.foodID = foodID;
        this.merchantID = merchantID;
        this.foodName = foodName;
        this.foodDes = foodDes;
        this.foodPrice = foodPrice;
        this.foodImg = foodImg;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDes() {
        return foodDes;
    }

    public void setFoodDes(String foodDes) {
        this.foodDes = foodDes;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodImg() {
        return foodImg;
    }

    public void setFoodImg(String foodImg) {
        this.foodImg = foodImg;
    }

    public FoodBean() {

    }

    private String foodID;
    private String merchantID;
    private String foodName;
    private String foodDes;
    private String foodPrice;
    private String foodImg;

    private String merchantName;
    private String merchantAvatar;
    private String rating;
    private int monthSales;

    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }

    public String getMerchantAvatar() { return merchantAvatar; }
    public void setMerchantAvatar(String merchantAvatar) { this.merchantAvatar = merchantAvatar; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public int getMonthSales() { return monthSales; }
    public void setMonthSales(int monthSales) { this.monthSales = monthSales; }
}
