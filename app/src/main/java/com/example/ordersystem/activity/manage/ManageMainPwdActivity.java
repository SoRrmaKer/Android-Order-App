package com.example.ordersystem.activity.manage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ordersystem.R;
import com.example.ordersystem.bean.BusinessBean;
import com.example.ordersystem.dao.AdminDao;

public class ManageMainPwdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_change_pwd);

        Toolbar toolbar = findViewById(R.id.pwd_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        EditText etOld = findViewById(R.id.pwd_old);
        EditText etNew = findViewById(R.id.pwd_new);
        EditText etConfirm = findViewById(R.id.pwd_confirm);
        Button btnSave = findViewById(R.id.pwd_btn);

        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        String account = sp.getString("account", "");
        if(account.isEmpty()) account = sp.getString("account", "root");

        final String currentId = account;

        btnSave.setOnClickListener(v -> {
            String oldPwd = etOld.getText().toString();
            String newPwd = etNew.getText().toString();
            String confirmPwd = etConfirm.getText().toString();

            BusinessBean business = AdminDao.getMerchantInfo(currentId);
            if (!business.getPwd().equals(oldPwd)) {
                Toast.makeText(this, "Old Password Incorrect", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPwd.isEmpty() || !newPwd.equals(confirmPwd)) {
                Toast.makeText(this, "New Passwords do not match or are empty", Toast.LENGTH_SHORT).show();
                return;
            }

            int result = AdminDao.updateMerchantPwd(currentId, newPwd);
            if (result == 1) {
                Toast.makeText(this, "Password Changed. Please Login Again.", Toast.LENGTH_LONG).show();
                // 简单的处理：直接返回，或者为了安全强制退出
                finish();
            } else {
                Toast.makeText(this, "Failed to change password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}