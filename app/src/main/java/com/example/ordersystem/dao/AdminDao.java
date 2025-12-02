package com.example.ordersystem.dao;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ordersystem.db.DBUntil;

public class AdminDao {
    public static SQLiteDatabase db = DBUntil.con;
    public static int saveMerchant(String id, String pwd, String des, String name, String type, String img) {
        String data[] = {id, pwd, des, name, type, img};

        try {
            db.execSQL("INSERT INTO d_business (s_id, s_pwd, s_name, s_describe, s_type, s_img) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    data);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int saveUser(String id, String pwd, String name, String sex, String address, String phone, String img) {
        String data[] = {id, pwd, name, sex, address, phone, img};

        try {
            db.execSQL("INSERT INTO d_user (s_id, s_pwd, s_name, s_sex, s_address, s_phone, s_img) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    data);

            return 1;
        } catch (Exception e) {
            return 0;
        }
    }


    public static int loginMerchant(String account, String pwd) {
        String data[] = {account, pwd};
        String sql = "select * from d_business where s_id=? and s_pwd=?";
        Cursor result = db.rawQuery(sql, data);
        while (result.moveToNext()) {
            return 1;
        }
        return 0;
    }

    public static int loginUser(String account, String pwd) {
        String data[] = {account, pwd};
        String sql = "select * from d_user where s_id=? and s_pwd=?";
        Cursor result = db.rawQuery(sql, data);
        while (result.moveToNext()) {
            return 1;
        }
        return 0;
    }

    @SuppressLint("Range")
    public static com.example.ordersystem.bean.BusinessBean getMerchantInfo(String id) {
        String[] data = {id};
        String sql = "select * from d_business where s_id=?";
        Cursor cursor = db.rawQuery(sql, data);

        com.example.ordersystem.bean.BusinessBean businessBean = null;
        if (cursor.moveToNext()) {
            businessBean = new com.example.ordersystem.bean.BusinessBean();
            businessBean.setId(cursor.getString(cursor.getColumnIndex("s_id")));
            businessBean.setPwd(cursor.getString(cursor.getColumnIndex("s_pwd")));
            businessBean.setName(cursor.getString(cursor.getColumnIndex("s_name")));
            businessBean.setDescribe(cursor.getString(cursor.getColumnIndex("s_describe")));
            businessBean.setType(cursor.getString(cursor.getColumnIndex("s_type")));
            businessBean.setImg(cursor.getString(cursor.getColumnIndex("s_img")));
        }
        cursor.close();
        return businessBean;
    }

    public static int updateMerchant(String id, String name, String des, String type, String img) {
        String[] data = {name, des, type, img, id};
        try {
            db.execSQL("UPDATE d_business set s_name=?, s_describe=?, s_type=?, s_img=? where s_id=?", data);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int updateMerchantPwd(String id, String newPwd) {
        String[] data = {newPwd, id};
        try {
            db.execSQL("UPDATE d_business set s_pwd=? where s_id=?", data);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int deleteMerchant(String id) {
        String[] data = {id};
        try {
            // 严谨起见，实际业务中通常还需要删除该商家下的所有商品，这里先只删除商家账号
            db.execSQL("DELETE from d_business where s_id=?", data);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
