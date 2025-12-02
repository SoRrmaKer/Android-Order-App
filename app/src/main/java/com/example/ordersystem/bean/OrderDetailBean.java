package com.example.ordersystem.bean;
import java.io.Serializable;

public class OrderDetailBean implements Serializable {
    private String foodName;
    private String price;
    private int count; // 数量
    private String img;
    private String foodId;

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

}