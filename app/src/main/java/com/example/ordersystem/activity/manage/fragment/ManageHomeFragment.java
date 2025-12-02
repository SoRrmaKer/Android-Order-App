package com.example.ordersystem.activity.manage.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.example.ordersystem.R;
import com.example.ordersystem.activity.manage.adapter.FoodListAdapter;
import com.example.ordersystem.bean.FoodBean;
import com.example.ordersystem.dao.FoodDao;
import com.example.ordersystem.until.Tools;

import java.util.List;


/**
 * A fragment representing a list of Items.
 */
public class ManageHomeFragment extends Fragment {
    ListView listView;
    FoodListAdapter adapter;
    String account;
    View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_manage_home, container, false);
        account = Tools.getAccount(getContext());

        listView = rootview.findViewById(R.id.man_food_listView);

        SearchView searchView = rootview.findViewById(R.id.man_home_food_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                loadData(newText);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                loadData(query);
                return false;
            }
        });

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData("");
    }
    private void loadData(String query) {
        if (listView == null) return;

        String searchText = (query == null) ? "" : query;
        List<FoodBean> list = FoodDao.getAllFoodList(account, searchText);

        FoodListAdapter adapter = new FoodListAdapter(getContext(), list);
        listView.setAdapter(adapter);
    }
}