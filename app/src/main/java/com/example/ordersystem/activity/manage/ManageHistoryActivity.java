package com.example.ordersystem.activity.manage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.ordersystem.R;
import com.example.ordersystem.activity.manage.adapter.OrderAdapter;
import com.example.ordersystem.bean.OrderBean;
import com.example.ordersystem.dao.OrderDao;

import java.util.List;

public class ManageHistoryActivity extends AppCompatActivity {

    private ListView listView;
    private OrderAdapter adapter;
    private String merchantId;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 复用之前的订单布局
        setContentView(R.layout.activity_manage_order);

        Toolbar toolbar = findViewById(R.id.man_order_toolbar);
        // 修改标题
        toolbar.setTitle("History Orders");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        merchantId = sp.getString("account", "");
        if(merchantId.isEmpty()) merchantId = sp.getString("account", "root");

        listView = findViewById(R.id.man_order_listview);
        searchView = findViewById(R.id.man_order_search);
        searchView.setQueryHint("Search History...");

        loadData("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadData(newText);
                return false;
            }
        });
    }

    private void loadData(String keyword) {
        List<OrderBean> list = OrderDao.getOrdersByMerchant(merchantId, keyword, true);

        if (adapter == null) {
            adapter = new OrderAdapter(this, list);
            listView.setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
    }
}