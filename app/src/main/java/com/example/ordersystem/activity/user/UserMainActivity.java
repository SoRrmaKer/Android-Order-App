package com.example.ordersystem.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ordersystem.R;
import com.example.ordersystem.activity.user.fragment.UserHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class UserMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        loadFragment(new UserHomeFragment());

        BottomNavigationView bottomNav = findViewById(R.id.user_bottom_nav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_user_home) {
                    loadFragment(new UserHomeFragment());
                    return true;
                } else if (id == R.id.menu_user_order) {
                    loadFragment(new com.example.ordersystem.activity.user.fragment.UserOrderFragment());
                    return true;
                } else if (id == R.id.menu_user_my) {
                    loadFragment(new com.example.ordersystem.activity.user.fragment.UserProfileFragment());
                    return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_frame_container, fragment);
        transaction.commit();
    }
}