package com.example.ordersystem.activity.user.fragment;

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
import com.example.ordersystem.activity.user.adapter.UserFoodListAdapter;
import com.example.ordersystem.bean.FoodBean;
import com.example.ordersystem.dao.FoodDao;

import java.util.List;

public class UserHomeFragment extends Fragment {

    private ListView listView;
    private UserFoodListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        listView = view.findViewById(R.id.user_home_listview);
        SearchView searchView = view.findViewById(R.id.user_home_search);

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

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            FoodBean food = (FoodBean) adapter.getItem(position);

            android.content.Intent intent = new android.content.Intent(getContext(), com.example.ordersystem.activity.user.UserShopDetailActivity.class);
            intent.putExtra("merchantId", food.getMerchantID());
            startActivity(intent);
        });

        return view;
    }

    private void loadData(String keyword) {
        List<FoodBean> list = FoodDao.getFoodListForUser(keyword);
        if (adapter == null) {
            adapter = new UserFoodListAdapter(getContext(), list);
            listView.setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
    }
}