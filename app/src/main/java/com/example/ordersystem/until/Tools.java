package com.example.ordersystem.until;

import android.content.Context;
import android.content.SharedPreferences;

public class Tools {
    public static String getAccount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String merchantId = sharedPreferences.getString("account", "root");
        return merchantId;
    }
}
