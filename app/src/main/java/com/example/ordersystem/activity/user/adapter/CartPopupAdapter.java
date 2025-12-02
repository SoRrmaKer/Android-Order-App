package com.example.ordersystem.activity.user.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ordersystem.R;
import com.example.ordersystem.bean.FoodBean;
import com.example.ordersystem.until.CartManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartPopupAdapter extends BaseAdapter {
    private Context context;
    private List<FoodBean> list;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onChange();
    }

    public CartPopupAdapter(Context context, OnCartChangeListener listener) {
        this.context = context;
        this.listener = listener;
        refreshData();
    }

    public void refreshData() {
        this.list = new ArrayList<>();
        Map<FoodBean, Integer> map = CartManager.getCartItems();
        for (FoodBean bean : map.keySet()) {
            this.list.add(bean);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() { return list == null ? 0 : list.size(); }
    @Override
    public Object getItem(int position) { return list.get(position); }
    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart_popup, parent, false);
            holder = new ViewHolder();
            holder.img = convertView.findViewById(R.id.cart_item_img);
            holder.name = convertView.findViewById(R.id.cart_item_name);
            holder.price = convertView.findViewById(R.id.cart_item_price);
            holder.count = convertView.findViewById(R.id.cart_item_count);
            holder.add = convertView.findViewById(R.id.cart_item_add);
            holder.minus = convertView.findViewById(R.id.cart_item_minus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FoodBean food = list.get(position);
        int num = CartManager.getFoodCount(food.getFoodID());

        holder.name.setText(food.getFoodName());
        holder.price.setText("¥ " + food.getFoodPrice());
        holder.count.setText(String.valueOf(num));

        if (food.getFoodImg() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(food.getFoodImg());
            if(bitmap != null) holder.img.setImageBitmap(bitmap);
        }

        holder.add.setOnClickListener(v -> {
            CartManager.add(food);
            refreshData(); // 刷新列表
            if (listener != null) listener.onChange();
        });

        holder.minus.setOnClickListener(v -> {
            CartManager.reduce(food);
            refreshData();
            if (listener != null) listener.onChange();
        });

        return convertView;
    }

    static class ViewHolder {
        TextView name, price, count;
        ImageView add, minus, img;
    }
}