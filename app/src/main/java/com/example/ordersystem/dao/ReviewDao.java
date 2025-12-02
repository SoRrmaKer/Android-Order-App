package com.example.ordersystem.dao;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ordersystem.bean.ReviewBean;
import com.example.ordersystem.db.DBUntil;

import java.util.ArrayList;
import java.util.List;

public class ReviewDao {
    public static SQLiteDatabase db = DBUntil.con;

    @SuppressLint("Range")
    public static List<ReviewBean> getReviewsByMerchant(String merchantId) {
        List<ReviewBean> list = new ArrayList<>();
        // 按时间倒序查询
        String sql = "SELECT * FROM d_reviews WHERE s_merchant_id = ? ORDER BY s_time DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{merchantId});

        while (cursor.moveToNext()) {
            ReviewBean bean = new ReviewBean();
            bean.setReviewId(cursor.getString(cursor.getColumnIndex("s_review_id")));
            bean.setUserId(cursor.getString(cursor.getColumnIndex("s_user_id")));
            bean.setRating(cursor.getFloat(cursor.getColumnIndex("s_rating")));
            bean.setContent(cursor.getString(cursor.getColumnIndex("s_content")));
            bean.setImgPath(cursor.getString(cursor.getColumnIndex("s_img")));
            bean.setTime(cursor.getString(cursor.getColumnIndex("s_time")));

            fillUserInfo(bean);

            list.add(bean);
        }
        cursor.close();
        return list;
    }

    @SuppressLint("Range")
    private static void fillUserInfo(ReviewBean bean) {
        Cursor c = db.rawQuery("SELECT s_name, s_img FROM d_user WHERE s_id = ?", new String[]{bean.getUserId()});
        if (c.moveToNext()) {
            bean.setUserName(c.getString(c.getColumnIndex("s_name")));
            bean.setUserAvatar(c.getString(c.getColumnIndex("s_img")));
        } else {
            bean.setUserName("Unknown User");
        }
        c.close();
    }
    public static boolean addReview(ReviewBean review) {
        try {
            db.execSQL("INSERT INTO d_reviews (s_review_id, s_order_id, s_user_id, s_merchant_id, s_rating, s_content, s_img, s_time) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{
                            review.getReviewId(),
                            review.getOrderId(),
                            review.getUserId(),
                            review.getMerchantId(),
                            review.getRating(),
                            review.getContent(),
                            review.getImgPath(),
                            review.getTime()
                    });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isOrderReviewed(String orderId) {
        Cursor cursor = db.rawQuery("SELECT count(*) FROM d_reviews WHERE s_order_id = ?", new String[]{orderId});
        if (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        cursor.close();
        return false;
    }
}