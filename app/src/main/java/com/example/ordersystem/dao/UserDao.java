package com.example.ordersystem.dao;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ordersystem.bean.UserBean;
import com.example.ordersystem.db.DBUntil;

public class UserDao {
    public static SQLiteDatabase db = DBUntil.con;
    @SuppressLint("Range")
    public static UserBean getUserInfo(String id) {
        Cursor c = db.rawQuery("SELECT * FROM d_user WHERE s_id = ?", new String[]{id});
        if (c.moveToNext()) {
            UserBean user = new UserBean();
            user.setId(c.getString(c.getColumnIndex("s_id")));
            user.setPwd(c.getString(c.getColumnIndex("s_pwd")));
            user.setName(c.getString(c.getColumnIndex("s_name")));
            user.setSex(c.getString(c.getColumnIndex("s_sex")));
            user.setAddress(c.getString(c.getColumnIndex("s_address")));
            user.setPhone(c.getString(c.getColumnIndex("s_phone")));
            user.setImg(c.getString(c.getColumnIndex("s_img")));
            c.close();
            return user;
        }
        c.close();
        return null;
    }

    public static int updateUser(String id, String name, String sex, String address, String phone, String img) {
        try {
            db.execSQL("UPDATE d_user SET s_name=?, s_sex=?, s_address=?, s_phone=?, s_img=? WHERE s_id=?",
                    new Object[]{name, sex, address, phone, img, id});
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int updatePwd(String id, String newPwd) {
        try {
            db.execSQL("UPDATE d_user SET s_pwd=? WHERE s_id=?", new Object[]{newPwd, id});
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int deleteUser(String id) {
        try {
            db.execSQL("DELETE FROM d_user WHERE s_id=?", new Object[]{id});
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}