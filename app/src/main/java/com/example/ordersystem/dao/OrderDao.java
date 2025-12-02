package com.example.ordersystem.dao;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ordersystem.bean.OrderBean;
import com.example.ordersystem.bean.OrderDetailBean;
import com.example.ordersystem.db.DBUntil;

import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    public static SQLiteDatabase db = DBUntil.con;
    @SuppressLint("Range")
    public static List<OrderBean> getOrdersByMerchant(String merchantId, String keyword, boolean isHistory) {
        List<OrderBean> orderList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM d_orders WHERE s_merchant_id = ?");
        if (isHistory) {
            sql.append(" AND s_status != 0");
        } else {
            sql.append(" AND s_status = 0");
        }

        List<String> argsList = new ArrayList<>();
        argsList.add(merchantId);

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (s_order_id LIKE ? OR s_user_id LIKE ?)");
            argsList.add("%" + keyword + "%");
            argsList.add("%" + keyword + "%");
        }

        sql.append(" ORDER BY s_order_time DESC");

        String[] args = argsList.toArray(new String[0]);
        Cursor cursor = db.rawQuery(sql.toString(), args);

        while (cursor.moveToNext()) {
            OrderBean order = new OrderBean();
            order.setOrderId(cursor.getString(cursor.getColumnIndex("s_order_id")));
            order.setOrderTime(cursor.getString(cursor.getColumnIndex("s_order_time")));
            order.setUserId(cursor.getString(cursor.getColumnIndex("s_user_id")));
            order.setAddress(cursor.getString(cursor.getColumnIndex("s_order_address")));
            order.setStatus(cursor.getInt(cursor.getColumnIndex("s_status")));
            order.setTotalPrice(cursor.getString(cursor.getColumnIndex("s_total_price")));

            String detailsId = cursor.getString(cursor.getColumnIndex("s_order_details_id"));
            getUserInfoForOrder(order);
            order.setDetailsList(getOrderDetails(detailsId));

            orderList.add(order);
        }
        cursor.close();
        return orderList;
    }
    @SuppressLint("Range")
    private static void getUserInfoForOrder(OrderBean order) {
        Cursor c = db.rawQuery("SELECT * FROM d_user WHERE s_id = ?", new String[]{order.getUserId()});
        if (c.moveToNext()) {
            order.setUserName(c.getString(c.getColumnIndex("s_name")));
            order.setUserPhone(c.getString(c.getColumnIndex("s_phone")));
            order.setUserAvatar(c.getString(c.getColumnIndex("s_img")));
        }
        c.close();
    }

    @SuppressLint("Range")
    private static List<OrderDetailBean> getOrderDetails(String detailsId) {
        List<OrderDetailBean> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM d_order_details WHERE s_details_id = ?", new String[]{detailsId});
        while (c.moveToNext()) {
            OrderDetailBean bean = new OrderDetailBean();
            bean.setFoodName(c.getString(c.getColumnIndex("s_food_name")));
            bean.setPrice(c.getString(c.getColumnIndex("s_food_price")));
            bean.setImg(c.getString(c.getColumnIndex("s_food_img")));
            try {
                bean.setCount(Integer.parseInt(c.getString(c.getColumnIndex("s_food_num"))));
            } catch (Exception e) { bean.setCount(1); }
            list.add(bean);
        }
        c.close();
        return list;
    }

    public static int updateOrderStatus(String orderId, int status) {
        try {
            db.execSQL("UPDATE d_orders SET s_status = ? WHERE s_order_id = ?", new Object[]{status, orderId});
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean createOrder(OrderBean order, String detailsId) {
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO d_orders (s_order_id, s_order_time, s_merchant_id, s_user_id, s_order_details_id, s_order_address, s_status, s_total_price) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{
                            order.getOrderId(),
                            order.getOrderTime(),
                            order.getMerchantId(),
                            order.getUserId(),
                            detailsId,
                            order.getAddress(),
                            0,
                            order.getTotalPrice()
                    });

            for (com.example.ordersystem.bean.OrderDetailBean detail : order.getDetailsList()) {
                db.execSQL("INSERT INTO d_order_details (s_details_id, s_food_id, s_food_name, s_food_des, s_food_price, s_food_num, s_food_img) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)",
                        new Object[]{
                                detailsId,
                                detail.getFoodId(),
                                detail.getFoodName(),
                                "No Des",
                                detail.getPrice(),
                                detail.getCount(),
                                detail.getImg()
                        });
            }

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    @SuppressLint("Range")
    public static List<OrderBean> getOrdersByUser(String userId, String keyword, boolean isHistory) {
        List<OrderBean> orderList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM d_orders WHERE s_user_id = ?");
        List<String> argsList = new ArrayList<>();
        argsList.add(userId);

        if (isHistory) {
            sql.append(" AND s_status != 0");
        } else {
            sql.append(" AND s_status = 0");
        }

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (s_order_id LIKE ? OR s_merchant_id IN (SELECT s_id FROM d_business WHERE s_name LIKE ?))");
            argsList.add("%" + keyword + "%");
            argsList.add("%" + keyword + "%");
        }

        sql.append(" ORDER BY s_order_time DESC");

        Cursor cursor = db.rawQuery(sql.toString(), argsList.toArray(new String[0]));
        while (cursor.moveToNext()) {
            OrderBean order = new OrderBean();
            order.setOrderId(cursor.getString(cursor.getColumnIndex("s_order_id")));
            order.setOrderTime(cursor.getString(cursor.getColumnIndex("s_order_time")));
            order.setMerchantId(cursor.getString(cursor.getColumnIndex("s_merchant_id")));
            order.setAddress(cursor.getString(cursor.getColumnIndex("s_order_address")));
            order.setStatus(cursor.getInt(cursor.getColumnIndex("s_status")));
            order.setTotalPrice(cursor.getString(cursor.getColumnIndex("s_total_price")));

            String detailsId = cursor.getString(cursor.getColumnIndex("s_order_details_id"));
            getMerchantInfoForOrder(order);
            order.setDetailsList(getOrderDetails(detailsId));
            order.setReviewed(ReviewDao.isOrderReviewed(order.getOrderId()));
            orderList.add(order);
        }
        cursor.close();
        return orderList;
    }

    @SuppressLint("Range")
    private static void getMerchantInfoForOrder(OrderBean order) {
        Cursor c = db.rawQuery("SELECT s_name, s_img FROM d_business WHERE s_id = ?", new String[]{order.getMerchantId()});
        if (c.moveToNext()) {
            order.setMerchantName(c.getString(c.getColumnIndex("s_name")));
            order.setMerchantAvatar(c.getString(c.getColumnIndex("s_img")));
        } else {
            order.setMerchantName("Unknown Shop");
        }
        c.close();
    }
}