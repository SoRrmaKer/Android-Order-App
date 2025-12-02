package com.example.ordersystem.activity.manage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ordersystem.R;
import com.example.ordersystem.activity.manage.adapter.CommentAdapter;
import com.example.ordersystem.bean.ReviewBean;
import com.example.ordersystem.dao.ReviewDao;

import java.util.List;

public class ManageCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_comment);

        Toolbar toolbar = findViewById(R.id.man_comment_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ListView listView = findViewById(R.id.man_comment_listview);

        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        String merchantId = sp.getString("account", "");
        if (merchantId.isEmpty()) merchantId = sp.getString("account", "root");

        List<ReviewBean> list = ReviewDao.getReviewsByMerchant(merchantId);

        CommentAdapter adapter = new CommentAdapter(this, list);
        listView.setAdapter(adapter);

        if (list == null || list.size() == 0) {
             Toast.makeText(this, "No reviews yet.", Toast.LENGTH_SHORT).show();
        }
    }
}