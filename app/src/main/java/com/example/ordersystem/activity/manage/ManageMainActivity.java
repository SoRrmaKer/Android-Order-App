package com.example.ordersystem.activity.manage;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ordersystem.R;
import com.example.ordersystem.activity.manage.fragment.ManageHomeFragment;
import com.example.ordersystem.activity.manage.fragment.ManageUserFragment; // 引入新创建的 Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ManageMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.manage_frame, new ManageHomeFragment())
                .commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_manage_bottom_menu);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                FragmentTransaction transaction = fragmentManager.beginTransaction();

                if(id == R.id.main_manage_bottom_menu_home) {
                    transaction.replace(R.id.manage_frame, new ManageHomeFragment());
                    transaction.commit();
                    return true;
                }

                if(id == R.id.main_manage_bottom_menu_add) {
                    Intent intent = new Intent(ManageMainActivity.this, ManageMainAddFoodActivity.class);
                    startActivity(intent);
                    return false;
                }

                if(id == R.id.main_manage_bottom_menu_my) {
                    transaction.replace(R.id.manage_frame, new ManageUserFragment());
                    transaction.commit();
                    return true;
                }

                return false;
            }
        });
    }
}