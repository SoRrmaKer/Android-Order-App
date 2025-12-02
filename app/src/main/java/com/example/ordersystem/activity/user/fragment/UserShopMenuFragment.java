package com.example.ordersystem.activity.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.ordersystem.R;
import com.example.ordersystem.activity.user.ConfirmOrderActivity;
import com.example.ordersystem.activity.user.adapter.CartPopupAdapter;
import com.example.ordersystem.activity.user.adapter.ShopFoodListAdapter;
import com.example.ordersystem.bean.FoodBean;
import com.example.ordersystem.dao.FoodDao;
import com.example.ordersystem.until.CartManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class UserShopMenuFragment extends Fragment {
    private String merchantId;
    private ListView listView;
    private ShopFoodListAdapter adapter;
    private View cartBarRoot;
    private TextView tvTotalPrice, tvBadgeCount;
    private View btnCheckout, btnCartPopup;

    public static UserShopMenuFragment newInstance(String merchantId) {
        UserShopMenuFragment fragment = new UserShopMenuFragment();
        Bundle args = new Bundle();
        args.putString("merchantId", merchantId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CartManager.clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_shop_menu, container, false);

        if (getArguments() != null) {
            merchantId = getArguments().getString("merchantId");
        }

        listView = view.findViewById(R.id.shop_menu_listview);
        SearchView searchView = view.findViewById(R.id.shop_menu_search);

        cartBarRoot = view.findViewById(R.id.layout_cart_bar);
        tvTotalPrice = view.findViewById(R.id.cart_total_price);
        tvBadgeCount = view.findViewById(R.id.cart_badge_count);
        btnCheckout = view.findViewById(R.id.btn_checkout);
        btnCartPopup = view.findViewById(R.id.btn_cart_popup);

        loadData("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { loadData(query); return false; }
            @Override
            public boolean onQueryTextChange(String newText) { loadData(newText); return false; }
        });

        btnCartPopup.setOnClickListener(v -> showCartPopup());

        btnCheckout.setOnClickListener(v -> {
            if (CartManager.getTotalCount() == 0) {
                Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getContext(), ConfirmOrderActivity.class);
            intent.putExtra("merchantId", merchantId);
            startActivity(intent);
        });
        return view;
    }

    private void loadData(String keyword) {
        List<FoodBean> list = FoodDao.getFoodListByMerchant(merchantId, keyword);
        if (adapter == null) {
            adapter = new ShopFoodListAdapter(getContext(), list);
            adapter.setOnCartUpdateListener(new ShopFoodListAdapter.OnCartUpdateListener() {
                @Override
                public void onCartUpdated() {
                    updateCartBar();
                }
            });
            listView.setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
    }

    private void updateCartBar() {
        int totalCount = CartManager.getTotalCount();

        if (totalCount > 0) {
            if (cartBarRoot != null) {
                cartBarRoot.setVisibility(View.VISIBLE);
            }
            tvBadgeCount.setText(String.valueOf(totalCount));
            tvTotalPrice.setText(String.format("¥ %.2f", CartManager.getTotalPrice()));
        } else {
            if (cartBarRoot != null) {
                cartBarRoot.setVisibility(View.GONE);
            }
        }
    }

    private void showCartPopup() {
        if (CartManager.getTotalCount() == 0) return;

        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cart_popup, null);

        ListView cartListView = dialogView.findViewById(R.id.cart_popup_listview);
        View btnClear = dialogView.findViewById(R.id.cart_clear_all);

        CartPopupAdapter popupAdapter = new CartPopupAdapter(getContext(), new CartPopupAdapter.OnCartChangeListener() {
            @Override
            public void onChange() {
                updateCartBar();
                adapter.notifyDataSetChanged();

                if (CartManager.getTotalCount() == 0) {
                    dialog.dismiss();
                }
            }
        });
        cartListView.setAdapter(popupAdapter);

        btnClear.setOnClickListener(v -> {
            CartManager.clear();
            updateCartBar();
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        dialog.setContentView(dialogView);
        dialog.show();
    }

}