package com.example.ordersystem.dao;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ordersystem.bean.FoodBean;
import com.example.ordersystem.db.DBUntil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FoodDao {
    public static SQLiteDatabase db = DBUntil.con;

    public static List<FoodBean> getAllFoodList() {
        List<FoodBean> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from d_food", null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String foodId = cursor.getString(cursor.getColumnIndex("s_food_id"));
            @SuppressLint("Range") String merchantId = cursor.getString(cursor.getColumnIndex("s_merchant_id"));
            @SuppressLint("Range") String foodName = cursor.getString(cursor.getColumnIndex("s_food_name"));
            @SuppressLint("Range") String foodDes = cursor.getString(cursor.getColumnIndex("s_food_des"));
            @SuppressLint("Range") String foodPrice = cursor.getString(cursor.getColumnIndex("s_food_price"));
            @SuppressLint("Range") String foodImg = cursor.getString(cursor.getColumnIndex("s_food_img"));
            FoodBean foodBean = new FoodBean();
            foodBean.setFoodID(foodId);
            foodBean.setFoodDes(foodDes);
            foodBean.setFoodImg(foodImg);
            foodBean.setFoodPrice(foodPrice);
            foodBean.setFoodName(foodName);
            foodBean.setMerchantID(merchantId);
            list.add(foodBean);
        }

        return list;
    }

    public static List<FoodBean> getAllFoodList(String merchant, String title) {
        String titleL = "%"+title+"%";
        String data[] = {merchant, titleL};

        List<FoodBean> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from d_food where s_merchant_id = ? and s_food_name like ?", data);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String foodId = cursor.getString(cursor.getColumnIndex("s_food_id"));
            @SuppressLint("Range") String merchantId = cursor.getString(cursor.getColumnIndex("s_merchant_id"));
            @SuppressLint("Range") String foodName = cursor.getString(cursor.getColumnIndex("s_food_name"));
            @SuppressLint("Range") String foodDes = cursor.getString(cursor.getColumnIndex("s_food_des"));
            @SuppressLint("Range") String foodPrice = cursor.getString(cursor.getColumnIndex("s_food_price"));
            @SuppressLint("Range") String foodImg = cursor.getString(cursor.getColumnIndex("s_food_img"));
            FoodBean foodBean = new FoodBean();
            foodBean.setFoodID(foodId);
            foodBean.setFoodDes(foodDes);
            foodBean.setFoodImg(foodImg);
            foodBean.setFoodPrice(foodPrice);
            foodBean.setFoodName(foodName);
            foodBean.setMerchantID(merchantId);
            list.add(foodBean);
        }

        return list;
    }


    public static FoodBean getAllFoodById(String id) {
        FoodBean foodBean = new FoodBean();
        Cursor cursor = db.rawQuery("select * from d_food where s_food_id=?", new String[]{id});
        if (cursor.moveToNext()) {
            @SuppressLint("Range") String foodId = cursor.getString(cursor.getColumnIndex("s_food_id"));
            @SuppressLint("Range") String merchantId = cursor.getString(cursor.getColumnIndex("s_merchant_id"));
            @SuppressLint("Range") String foodName = cursor.getString(cursor.getColumnIndex("s_food_name"));
            @SuppressLint("Range") String foodDes = cursor.getString(cursor.getColumnIndex("s_food_des"));
            @SuppressLint("Range") String foodPrice = cursor.getString(cursor.getColumnIndex("s_food_price"));
            @SuppressLint("Range") String foodImg = cursor.getString(cursor.getColumnIndex("s_food_img"));
            foodBean.setFoodID(foodId);
            foodBean.setFoodDes(foodDes);
            foodBean.setFoodImg(foodImg);
            foodBean.setFoodPrice(foodPrice);
            foodBean.setFoodName(foodName);
            foodBean.setMerchantID(merchantId);
            return foodBean;
        } else {
            return null;
        }
    }


    // Monthly Sale Number
    @SuppressLint("Range")
    public static int getMonthSale(String foodId) {
        Cursor rs = db.rawQuery("SELECT * FROM d_orders " +
                "WHERE strftime('%Y-%m', s_order_time) = strftime('%Y-%m', 'now');", null);
        List<String> list = new ArrayList<>();
        while (rs.moveToNext()) {
            String temp = rs.getString(rs.getColumnIndex("s_order_details_id"));
            list.add(temp);
        }
        int saleNum = 0;
        for( String s : list) {
            saleNum = saleNum + getOrderDetails(s, foodId);
        }
        return saleNum;
    }


    // Get Order Details
    @SuppressLint("Range")
    public static int getOrderDetails(String orderId, String foodId) {
        String data[] = {orderId, foodId};
        Cursor rs = db.rawQuery("SELECT * FROM d_order_details where s_details_id = ? and s_food_id = ?", data);
        List<String> list = new ArrayList<>();
        while (rs.moveToNext()) {
            int temp = rs.getInt(rs.getColumnIndex("s_food_num"));
            return temp;
        }
        return 0;
    }

    public static int addFood(String merchantId, String foodName, String des, String foodPrice, String img) {
        String id = UUID.randomUUID().toString().replace("-", "");

        String data[] = {id, merchantId, foodName, des, foodPrice, img};

        try {
            db.execSQL("INSERT INTO d_food (s_food_id, s_merchant_id, s_food_name, s_food_des, s_food_price, s_food_img) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    data);
            return 1;
        } catch (Exception e) {
            return 0;
        }

    }

    public static int delFood(String foodId) {
        String data[] = {foodId};

        try {
            db.execSQL("DELETE from d_food where s_food_id = ?",
                    data);
            return 1;
        } catch (Exception e) {
            return 0;
        }

    }


    public static int updateFood(String foodId, String foodName, String des, String foodPrice, String img) {
        String data[] = {foodName, des, foodPrice, img, foodId};

        try {
            db.execSQL("UPDATE d_food set s_food_name = ?, s_food_des = ?,s_food_price = ?,s_food_img = ? where s_food_id = ?",
                    data);
            return 1;
        } catch (Exception e) {
            return 0;
        }

    }

    @SuppressLint("Range")
    public static List<FoodBean> getFoodListForUser(String keyword) {
        List<FoodBean> list = new ArrayList<>();
        String sql = "select * from d_food";
        String[] args = null;

        if (keyword != null && !keyword.isEmpty()) {
            sql += " where s_food_name like ?";
            args = new String[]{"%" + keyword + "%"};
        }

        Cursor cursor = db.rawQuery(sql, args);
        while (cursor.moveToNext()) {
            FoodBean food = new FoodBean();
            food.setFoodID(cursor.getString(cursor.getColumnIndex("s_food_id")));
            food.setMerchantID(cursor.getString(cursor.getColumnIndex("s_merchant_id")));
            food.setFoodName(cursor.getString(cursor.getColumnIndex("s_food_name")));
            food.setFoodDes(cursor.getString(cursor.getColumnIndex("s_food_des")));
            food.setFoodPrice(cursor.getString(cursor.getColumnIndex("s_food_price")));
            food.setFoodImg(cursor.getString(cursor.getColumnIndex("s_food_img")));

            food.setMonthSales(getMonthSale(food.getFoodID()));

            getMerchantInfoForFood(food);

            getMerchantRating(food);

            list.add(food);
        }
        return list;
    }

    @SuppressLint("Range")
    private static void getMerchantInfoForFood(FoodBean food) {
        Cursor c = db.rawQuery("select s_name, s_img from d_business where s_id = ?", new String[]{food.getMerchantID()});
        if (c.moveToNext()) {
            food.setMerchantName(c.getString(c.getColumnIndex("s_name")));
            food.setMerchantAvatar(c.getString(c.getColumnIndex("s_img")));
        } else {
            food.setMerchantName("Unknown Shop");
        }
        c.close();
    }

    @SuppressLint("Range")
    private static void getMerchantRating(FoodBean food) {
        Cursor c = db.rawQuery("select avg(s_rating) from d_reviews where s_merchant_id = ?", new String[]{food.getMerchantID()});
        if (c.moveToNext()) {
            float rate = c.getFloat(0);
            if (rate == 0) rate = 5.0f; // 默认5分
            food.setRating(String.format("%.1f", rate));
        } else {
            food.setRating("5.0");
        }
        c.close();
    }

    @SuppressLint("Range")
    public static List<FoodBean> getFoodListByMerchant(String merchantId, String keyword) {
        List<FoodBean> list = new ArrayList<>();
        String sql = "select * from d_food where s_merchant_id = ?";
        List<String> argsList = new ArrayList<>();
        argsList.add(merchantId);

        if (keyword != null && !keyword.isEmpty()) {
            sql += " and s_food_name like ?";
            argsList.add("%" + keyword + "%");
        }

        Cursor cursor = db.rawQuery(sql, argsList.toArray(new String[0]));
        while (cursor.moveToNext()) {
            FoodBean food = new FoodBean();
            food.setFoodID(cursor.getString(cursor.getColumnIndex("s_food_id")));
            food.setMerchantID(cursor.getString(cursor.getColumnIndex("s_merchant_id")));
            food.setFoodName(cursor.getString(cursor.getColumnIndex("s_food_name")));
            food.setFoodDes(cursor.getString(cursor.getColumnIndex("s_food_des")));
            food.setFoodPrice(cursor.getString(cursor.getColumnIndex("s_food_price")));
            food.setFoodImg(cursor.getString(cursor.getColumnIndex("s_food_img")));

            food.setMonthSales(getMonthSale(food.getFoodID()));
            list.add(food);
        }
        return list;
    }
}
