package com.example.ordersystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ordersystem.activity.manage.ManageMainActivity;
import com.example.ordersystem.activity.manage.RegisterMainActivity;
import com.example.ordersystem.activity.user.RegisterUserActivity;
import com.example.ordersystem.activity.user.UserMainActivity;
import com.example.ordersystem.dao.AdminDao;
import com.example.ordersystem.db.DBUntil;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        DBUntil dbUntil = new DBUntil(this);
        DBUntil.con = dbUntil.getWritableDatabase();

        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("account", "root");
        edit.apply();

//        Intent intent = new Intent(MainActivity.this, UserMainActivity.class);
//        startActivity(intent);

        RadioButton merRadio = findViewById(R.id.login_merchant);
        merRadio.setChecked(true); // Set the Merchant Button Checked

        Button merSignUp = findViewById(R.id.btn_merchant);
        merSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to Merchant View
                Intent intent = new Intent(MainActivity.this, RegisterMainActivity.class);
                startActivity(intent);
            }
        });

        Button userSignUp = findViewById(R.id.btn_user);
        userSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to User View
                Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });

        // Login
        EditText accountText = findViewById(R.id.login_account);
        EditText pwdText = findViewById(R.id.login_pwd);
        Button userLogin = findViewById(R.id.btn_login);
        RadioButton role = findViewById(R.id.login_merchant);



        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountText.getText().toString();
                String pwd = pwdText.getText().toString();
                if(account.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter Account", Toast.LENGTH_SHORT).show();
                } else if(pwd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();

                    if(role.isChecked()) {
                        int a = AdminDao.loginMerchant(account, pwd);
                        if(a == 1) {
                            edit.putString("account", account);
                            edit.apply();
                            Toast.makeText(MainActivity.this, "Merchant Login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, com.example.ordersystem.activity.manage.ManageMainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Merchant Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        int a = AdminDao.loginUser(account, pwd);
                        if (a == 1) {
                            edit.putString("account", account);
                            edit.apply();
                            Toast.makeText(MainActivity.this, "User Login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, com.example.ordersystem.activity.user.UserMainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "User Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}