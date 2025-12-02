package com.example.ordersystem.activity.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ordersystem.R;
import com.example.ordersystem.activity.user.fragment.UserShopMenuFragment;
import com.example.ordersystem.activity.user.fragment.UserShopReviewFragment;
import com.example.ordersystem.bean.BusinessBean;
import com.example.ordersystem.dao.AdminDao;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class UserShopDetailActivity extends AppCompatActivity {

    private String merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shop_detail);

        merchantId = getIntent().getStringExtra("merchantId");

        initHeader();
        initTabs();
    }

    private void initHeader() {
        BusinessBean bean = AdminDao.getMerchantInfo(merchantId);

        ImageView avatar = findViewById(R.id.shop_detail_avatar);
        TextView name = findViewById(R.id.shop_detail_name);
        TextView des = findViewById(R.id.shop_detail_des);

        if (bean != null) {
            name.setText(bean.getName());
            des.setText(bean.getDescribe());
            if (bean.getImg() != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(bean.getImg());
                if (bitmap != null) avatar.setImageBitmap(bitmap);
            }
        }
    }

    private void initTabs() {
        TabLayout tabLayout = findViewById(R.id.shop_detail_tabs);
        ViewPager2 viewPager = findViewById(R.id.shop_detail_viewpager);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    return UserShopMenuFragment.newInstance(merchantId);
                } else {
                    return UserShopReviewFragment.newInstance(merchantId);
                }
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Menu");
            else tab.setText("Reviews");
        }).attach();
    }
}