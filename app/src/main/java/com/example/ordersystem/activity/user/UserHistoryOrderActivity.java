package com.example.ordersystem.activity.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.ordersystem.R;
import com.example.ordersystem.activity.user.adapter.UserOrderAdapter;
import com.example.ordersystem.bean.OrderBean;
import com.example.ordersystem.dao.OrderDao;

import java.util.List;

public class UserHistoryOrderActivity extends AppCompatActivity {

    private ListView listView;
    private UserOrderAdapter adapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history_order);

        Toolbar toolbar = findViewById(R.id.history_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        listView = findViewById(R.id.history_listview);
        SearchView searchView = findViewById(R.id.history_search);

        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        userId = sp.getString("account", "");
        if (userId.isEmpty()) userId = sp.getString("account", "root");

        loadData("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { loadData(query); return false; }
            @Override
            public boolean onQueryTextChange(String newText) { loadData(newText); return false; }
        });
    }

    private void loadData(String keyword) {
        List<OrderBean> list = OrderDao.getOrdersByUser(userId, keyword, true);
        if (adapter == null) {
            adapter = new UserOrderAdapter(this, list);
            listView.setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
    }
}