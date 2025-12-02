package com.example.ordersystem.activity.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ordersystem.R;
import com.example.ordersystem.bean.FoodBean;
import com.example.ordersystem.bean.OrderBean;
import com.example.ordersystem.bean.OrderDetailBean;
import com.example.ordersystem.dao.AdminDao;
import com.example.ordersystem.dao.OrderDao;
import com.example.ordersystem.db.DBUntil;
import com.example.ordersystem.until.CartManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ConfirmOrderActivity extends AppCompatActivity {

    private String userId;
    private String merchantId;
    private TextView tvName, tvPhone, tvAddress;
    private TextView tvTotalPrice;
    private LinearLayout itemsContainer;
    private String currentName, currentPhone, currentAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        merchantId = getIntent().getStringExtra("merchantId");

        // 获取当前用户
        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        userId = sp.getString("account", "");
        if(userId.isEmpty()) userId = sp.getString("account", "root");

        initView();
        loadUserInfo();
        loadAddress();
        loadCartItems();
    }

    private void initView() {
        // 顶部信息
        ImageView ivAvatar = findViewById(R.id.confirm_user_avatar);
        TextView tvUserName = findViewById(R.id.confirm_user_name);
        TextView tvTime = findViewById(R.id.confirm_order_time);

        // 设置时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tvTime.setText(sdf.format(new Date()));

        // 地址信息
        tvName = findViewById(R.id.confirm_addr_name);
        tvPhone = findViewById(R.id.confirm_addr_phone);
        tvAddress = findViewById(R.id.confirm_addr_detail);
        ImageView btnEdit = findViewById(R.id.btn_edit_address);

        // 底部
        tvTotalPrice = findViewById(R.id.confirm_total_price);
        Button btnCancel = findViewById(R.id.btn_cancel_pay);
        Button btnPay = findViewById(R.id.btn_confirm_pay);
        itemsContainer = findViewById(R.id.confirm_items_container);

        btnEdit.setOnClickListener(v -> showEditDialog());
        btnCancel.setOnClickListener(v -> finish());
        btnPay.setOnClickListener(v -> processPayment());
    }

    private void loadUserInfo() {
        Cursor c = DBUntil.con.rawQuery("select s_name, s_img from d_user where s_id=?", new String[]{userId});
        if (c.moveToNext()) {
            TextView tvUser = findViewById(R.id.confirm_user_name);
            tvUser.setText(c.getString(0));
            String path = c.getString(1);
            if (path != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                ImageView iv = findViewById(R.id.confirm_user_avatar);
                if (bitmap != null) iv.setImageBitmap(bitmap);
            }
        }
        c.close();
    }

    private void loadAddress() {
        SharedPreferences sp = getSharedPreferences("addr_pref", Context.MODE_PRIVATE);
        currentName = sp.getString("name", "");
        currentPhone = sp.getString("phone", "");
        currentAddress = sp.getString("address", "");

        if (currentName.isEmpty()) {
            Cursor c = DBUntil.con.rawQuery("select s_name, s_phone, s_address from d_user where s_id=?", new String[]{userId});
            if (c.moveToNext()) {
                currentName = c.getString(0);
                currentPhone = c.getString(1);
                currentAddress = c.getString(2);
            }
            c.close();
        }
        updateAddressUI();
    }

    private void updateAddressUI() {
        tvName.setText("Recipient: " + currentName);
        tvPhone.setText("Phone: " + currentPhone);
        tvAddress.setText("Address: " + currentAddress);
    }

    private void showEditDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_address, null);
        EditText etName = view.findViewById(R.id.dialog_name);
        EditText etPhone = view.findViewById(R.id.dialog_phone);
        EditText etAddress = view.findViewById(R.id.dialog_address);

        etName.setText(currentName);
        etPhone.setText(currentPhone);
        etAddress.setText(currentAddress);

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    currentName = etName.getText().toString();
                    currentPhone = etPhone.getText().toString();
                    currentAddress = etAddress.getText().toString();

                    SharedPreferences.Editor editor = getSharedPreferences("addr_pref", Context.MODE_PRIVATE).edit();
                    editor.putString("name", currentName);
                    editor.putString("phone", currentPhone);
                    editor.putString("address", currentAddress);
                    editor.apply();

                    updateAddressUI();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loadCartItems() {
        itemsContainer.removeAllViews();
        Map<FoodBean, Integer> cartItems = CartManager.getCartItems();

        for (Map.Entry<FoodBean, Integer> entry : cartItems.entrySet()) {
            FoodBean food = entry.getKey();
            int count = entry.getValue();

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(0, 10, 0, 10);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);

            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(130, 130);
            imgParams.setMargins(0, 0, 25, 0);
            iv.setLayoutParams(imgParams);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (food.getFoodImg() != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(food.getFoodImg());
                if (bitmap != null) iv.setImageBitmap(bitmap);
            }
            row.addView(iv);

            TextView name = new TextView(this);
            name.setText(food.getFoodName());
            name.setTextSize(15);
            name.setTextColor(getResources().getColor(android.R.color.black));
            name.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1));

            TextView priceInfo = new TextView(this);
            priceInfo.setText("x" + count + "   ¥" + food.getFoodPrice());
            priceInfo.setTextColor(getResources().getColor(R.color.ThemeBlue));
            priceInfo.setTextSize(14);
            priceInfo.setPadding(20, 0, 0, 0);

            row.addView(name);
            row.addView(priceInfo);
            itemsContainer.addView(row);

            View line = new View(this);
            line.setLayoutParams(new LinearLayout.LayoutParams(-1, 1));
            line.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            line.setAlpha(0.2f);
            itemsContainer.addView(line);
        }

        tvTotalPrice.setText(String.format("¥ %.2f", CartManager.getTotalPrice()));
    }

    private void processPayment() {
        if (currentAddress.isEmpty() || currentPhone.isEmpty()) {
            Toast.makeText(this, "Please complete address info", Toast.LENGTH_SHORT).show();
            return;
        }

        String orderId = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String detailsId = UUID.randomUUID().toString();

        OrderBean order = new OrderBean();
        order.setOrderId(orderId);
        order.setMerchantId(merchantId);
        order.setUserId(userId);
        order.setOrderTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        order.setAddress(currentAddress + " (" + currentName + " " + currentPhone + ")");
        order.setTotalPrice(String.format("%.2f", CartManager.getTotalPrice()));
        order.setStatus(0);

        // 构建详情列表
        List<OrderDetailBean> details = new ArrayList<>();
        for (Map.Entry<FoodBean, Integer> entry : CartManager.getCartItems().entrySet()) {
            FoodBean food = entry.getKey();
            OrderDetailBean detail = new OrderDetailBean();
            detail.setFoodName(food.getFoodName());
            detail.setPrice(food.getFoodPrice());
            detail.setCount(entry.getValue());
            detail.setFoodId(food.getFoodID());
            detail.setImg(food.getFoodImg());

            details.add(detail);
        }
        order.setDetailsList(details);

        if (OrderDao.createOrder(order, detailsId)) {
            Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_LONG).show();
            CartManager.clear();

            Intent intent = new Intent(ConfirmOrderActivity.this, UserMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
        }
    }
}