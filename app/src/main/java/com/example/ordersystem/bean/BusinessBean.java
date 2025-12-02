package com.example.ordersystem.bean;

import java.io.Serializable;

public class BusinessBean implements Serializable {
    private String id;
    private String pwd;
    private String name;
    private String describe;
    private String type;
    private String img;

    public BusinessBean() {
    }

    public BusinessBean(String id, String pwd, String name, String describe, String type, String img) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.describe = describe;
        this.type = type;
        this.img = img;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPwd() { return pwd; }
    public void setPwd(String pwd) { this.pwd = pwd; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescribe() { return describe; }
    public void setDescribe(String describe) { this.describe = describe; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }
}