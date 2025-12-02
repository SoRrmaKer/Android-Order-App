package com.example.ordersystem.activity.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ordersystem.R;
import com.example.ordersystem.bean.UserBean;
import com.example.ordersystem.dao.UserDao;

public class UserChangePwdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_pwd);

        Toolbar toolbar = findViewById(R.id.user_pwd_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        EditText etOld = findViewById(R.id.u_pwd_old);
        EditText etNew = findViewById(R.id.u_pwd_new);
        EditText etConfirm = findViewById(R.id.u_pwd_confirm);
        Button btnSave = findViewById(R.id.u_pwd_btn);

        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        String userId = sp.getString("account", "");
        if (userId.isEmpty()) userId = sp.getString("account", "root");

        final String currentId = userId;

        btnSave.setOnClickListener(v -> {
            String oldPwd = etOld.getText().toString();
            String newPwd = etNew.getText().toString();
            String confirmPwd = etConfirm.getText().toString();

            UserBean user = UserDao.getUserInfo(currentId);
            if (user == null || !user.getPwd().equals(oldPwd)) {
                Toast.makeText(this, "Old Password Incorrect", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPwd.isEmpty() || !newPwd.equals(confirmPwd)) {
                Toast.makeText(this, "New Password does not match or is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (UserDao.updatePwd(currentId, newPwd) == 1) {
                Toast.makeText(this, "Password Changed.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}