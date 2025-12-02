package com.example.ordersystem.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.example.ordersystem.R;
import com.example.ordersystem.activity.user.RegisterUserActivity;
import com.example.ordersystem.until.FileImgUntil;

// Connect to SQL
public class DBUntil extends SQLiteOpenHelper {
    private static final int version = 11;
    private static final String databaseName = "db_takeaway.db";
    private Context context;
    public static SQLiteDatabase con;
    public DBUntil(Context context) {
        super(context, databaseName, null, version, null);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create DATABASE
        db.execSQL("PRAGMA foreign_keys = false");
        db.execSQL("drop table if exists d_business");
        db.execSQL("create table d_business(s_id vchar(20) primary key," +
                "s_pwd vchar(20)," +
                "s_name vchar(20)," +
                "s_describe vchar(255)," +
                "s_type vchar(20)," +
                "s_img vchar(255))");

        String path = FileImgUntil.getPath();
        Drawable defaultDrawable = ContextCompat.getDrawable(context, R.drawable.uploadimg);
        Bitmap bitmapDef = ((BitmapDrawable) defaultDrawable).getBitmap();

        FileImgUntil.saveBitmapAsync(bitmapDef, path);

        db.execSQL("INSERT INTO d_business (s_id, s_pwd, s_name, s_describe, s_type, s_img) " +
                   "VALUES (?, ?, ?, ?, ?, ?)",
                    new Object[]{"root", "123456", "Teeta Official", "SUPER MANAGER", "Manager", path});

        // User Table
        db.execSQL("drop table if exists d_user");
        db.execSQL("create table d_user(s_id vchar(20) primary key," +
                "s_pwd vchar(20)," +
                "s_name vchar(20)," +
                "s_sex vchar(255)," +
                "s_address vchar(255)," +
                "s_phone vchar(20)," +
                "s_img vchar(255))");

        db.execSQL("INSERT INTO d_user (s_id, s_pwd, s_name, s_sex, s_address, s_phone, s_img) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"admin", "123456", "Teeta Test", "Male", "Shibuya", "1324123241", path});

        // Food Table
        db.execSQL("drop table if exists d_food");
        db.execSQL("create table d_food(s_food_id vchar(20) primary key," +
                "s_merchant_id vchar(20)," +
                "s_food_name vchar(20)," +
                "s_food_des vchar(255)," +
                "s_food_price vchar(255)," +
                "s_food_img vchar(255))");

        String foodImg = FileImgUntil.getPath();
        FileImgUntil.saveSystemImgToPath(context, R.drawable.jollibee, foodImg);

        String foodImg2 = FileImgUntil.getPath();
        FileImgUntil.saveSystemImgToPath(context, R.drawable.mcd, foodImg2);

        db.execSQL("INSERT INTO d_food (s_food_id, s_merchant_id, s_food_name, s_food_des, s_food_price, s_food_img) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                new Object[]{"2", "1234131", "MacDonald", "Greatest Fast Food", "28.30", foodImg2});

        // Sale Table
        db.execSQL("drop table if exists d_orders");
        db.execSQL("create table d_orders(s_order_id vchar(20) primary key," +
                "s_order_time vchar(30)," +
                "s_merchant_id vchar(20)," +
                "s_user_id vchar(255)," +
                "s_order_details_id vchar(20)," +
                "s_order_address vchar(255),"+
                "s_status integer," +
                "s_total_price vchar(20))");

        db.execSQL("INSERT INTO d_orders (s_order_id, s_order_time, s_merchant_id, s_user_id, s_order_details_id, s_order_address, s_status, s_total_price) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"1", "2025-12-23 12:42:23", "Jollibee", "admin", "1", "Shibuya, Tokyo", 0, "87.00"});

        // Order Detail
        db.execSQL("drop table if exists d_order_details");
        db.execSQL("create table d_order_details(s_details_id vchar(20)," +
                "s_food_id vchar(20)," +
                "s_food_name vchar(20)," +
                "s_food_des vchar(255)," +
                "s_food_price vchar(255)," +
                "s_food_num varchar(200)," +
                "s_food_img vchar(255))");

        db.execSQL("INSERT INTO d_order_details (s_details_id, s_food_id, s_food_name, s_food_des, s_food_price, s_food_num, s_food_img) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"1", "1", "Jollibee", "Delicious Chicken", "58.50", "2", foodImg});

        db.execSQL("INSERT INTO d_order_details (s_details_id, s_food_id, s_food_name, s_food_des, s_food_price, s_food_num, s_food_img) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"1", "2", "MacDonalds", "MacFish", "28.50", "1", foodImg});

        // comments
        db.execSQL("drop table if exists d_reviews");
        db.execSQL("create table d_reviews(s_review_id vchar(20) primary key," +
                "s_order_id vchar(20)," +
                "s_user_id vchar(20)," +
                "s_merchant_id vchar(20)," +
                "s_rating float," +
                "s_content vchar(255)," +
                "s_img vchar(255)," +
                "s_time vchar(30))");

        db.execSQL("INSERT INTO d_reviews (s_review_id, s_order_id, s_user_id, s_merchant_id, s_rating, s_content, s_img, s_time) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"rev_001", "1", "admin", "root", 4.5, "The food was delicious and delivery was fast!", foodImg, "2023-12-08 18:30:00"});



        db.execSQL("PRAGMA foreign_keys = True");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
