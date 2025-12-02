package com.example.ordersystem.bean;

import java.io.Serializable;

public class UserBean implements Serializable {
    private String id;
    private String pwd;
    private String name;
    private String sex;
    private String address;
    private String phone;
    private String img;

    public UserBean() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPwd() { return pwd; }
    public void setPwd(String pwd) { this.pwd = pwd; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }
}