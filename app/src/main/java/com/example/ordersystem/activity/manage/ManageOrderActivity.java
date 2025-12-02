package com.example.ordersystem.activity.manage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.ordersystem.R;
import com.example.ordersystem.activity.manage.adapter.OrderAdapter;
import com.example.ordersystem.bean.OrderBean;
import com.example.ordersystem.dao.OrderDao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.ordersystem.db.DBUntil;
import java.util.UUID;

public class ManageOrderActivity extends AppCompatActivity {

    private ListView listView;
    private OrderAdapter adapter;
    private String merchantId;
    private SearchView searchView;

    private void insertTestData() {
        SQLiteDatabase db = DBUntil.con;

        // 1. 尝试获取一张现有的图片路径，保证测试数据显示图片不报错
        String validImgPath = "";
        Cursor c = db.rawQuery("select s_food_img from d_food limit 1", null);
        if (c.moveToNext()) {
            validImgPath = c.getString(0);
        }
        c.close();

        // 如果没找到图片，就忽略图片显示
        if (validImgPath == null) validImgPath = "";

        // 2. 准备ID
        String orderId = "TEST_" + System.currentTimeMillis(); // 唯一订单号
        String detailsId = UUID.randomUUID().toString();     // 关联详情的ID

        // 获取当前商户ID (为了让您登录后能看到)
        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        String myMerchantId = sp.getString("account", "");
        if(myMerchantId.isEmpty()) myMerchantId = sp.getString("account", "root");

        // 3. 插入订单主表 (d_orders)
        // 对应截图：2023-12-07, 用户admin, 地址江苏南京, 状态0(待处理), 总价754.68
        db.execSQL("INSERT INTO d_orders (s_order_id, s_order_time, s_merchant_id, s_user_id, s_order_details_id, s_order_address, s_status, s_total_price) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{
                        orderId,
                        "2023-12-07",
                        myMerchantId,
                        "admin",      // 确保 d_user 表里有 id 为 admin 的用户
                        detailsId,
                        "Jiangsu, Nanjing, Cartoon",
                        0,            // 0 代表 Pending (待处理)
                        "754.68"
                });

        // 4. 插入订单详情 (d_order_details) - 插入三条数据模拟截图

        // 商品 1: 东北麻辣烫1 x 11
        db.execSQL("INSERT INTO d_order_details (s_details_id, s_food_id, s_food_name, s_food_des, s_food_price, s_food_num, s_food_img) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                new Object[]{detailsId, "food_001", "Northwest Spicy 1", "Spicy and Hot", "218.46", "11", validImgPath});

        // 商品 2: 东北麻辣烫2 x 13
        db.execSQL("INSERT INTO d_order_details (s_details_id, s_food_id, s_food_name, s_food_des, s_food_price, s_food_num, s_food_img) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                new Object[]{detailsId, "food_002", "Northwest Spicy 2", "Spicy and Hot", "258.18", "13", validImgPath});

        // 商品 3: 东北麻辣烫3 x 14
        db.execSQL("INSERT INTO d_order_details (s_details_id, s_food_id, s_food_name, s_food_des, s_food_price, s_food_num, s_food_img) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                new Object[]{detailsId, "food_003", "Northwest Spicy 3", "Spicy and Hot", "278.04", "14", validImgPath});

        android.widget.Toast.makeText(this, "Test Data Inserted!", android.widget.Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);

        Toolbar toolbar = findViewById(R.id.man_order_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        merchantId = sp.getString("account", "");
        if(merchantId.isEmpty()) merchantId = sp.getString("account", "root");

        listView = findViewById(R.id.man_order_listview);
        searchView = findViewById(R.id.man_order_search);

//        insertTestData();

        loadData("");

        // 搜索监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadData(newText);
                return false;
            }
        });
    }

    private void loadData(String keyword) {
        List<OrderBean> list = OrderDao.getOrdersByMerchant(merchantId, keyword, false);
        if (adapter == null) {
            adapter = new OrderAdapter(this, list);
            listView.setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
    }
}