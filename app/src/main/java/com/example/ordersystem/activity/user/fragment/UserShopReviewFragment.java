package com.example.ordersystem.activity.user.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ordersystem.R;
import com.example.ordersystem.activity.manage.adapter.CommentAdapter;
import com.example.ordersystem.bean.ReviewBean;
import com.example.ordersystem.dao.ReviewDao;

import java.util.List;

public class UserShopReviewFragment extends Fragment {
    private String merchantId;

    public static UserShopReviewFragment newInstance(String merchantId) {
        UserShopReviewFragment fragment = new UserShopReviewFragment();
        Bundle args = new Bundle();
        args.putString("merchantId", merchantId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_shop_review, container, false);

        if (getArguments() != null) {
            merchantId = getArguments().getString("merchantId");
        }

        ListView listView = view.findViewById(R.id.shop_review_listview);

        List<ReviewBean> list = ReviewDao.getReviewsByMerchant(merchantId);
        CommentAdapter adapter = new CommentAdapter(getContext(), list);
        listView.setAdapter(adapter);

        return view;
    }
}