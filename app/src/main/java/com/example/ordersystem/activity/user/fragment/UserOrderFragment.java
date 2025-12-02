package com.example.ordersystem.activity.user.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.ordersystem.R;
import com.example.ordersystem.activity.user.adapter.UserOrderAdapter;
import com.example.ordersystem.bean.OrderBean;
import com.example.ordersystem.dao.OrderDao;

import java.util.List;

public class UserOrderFragment extends Fragment {

    private ListView listView;
    private UserOrderAdapter adapter;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_order, container, false);

        listView = view.findViewById(R.id.user_order_listview);
        SearchView searchView = view.findViewById(R.id.user_order_search);

        SharedPreferences sp = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        userId = sp.getString("account", "");
        if (userId.isEmpty()) userId = sp.getString("account", "root");

        loadData("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { loadData(query); return false; }
            @Override
            public boolean onQueryTextChange(String newText) { loadData(newText); return false; }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData("");
    }

    private void loadData(String keyword) {
        List<OrderBean> list = OrderDao.getOrdersByUser(userId, keyword, false);
        if (adapter == null) {
            adapter = new UserOrderAdapter(getContext(), list);
            listView.setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
    }
}