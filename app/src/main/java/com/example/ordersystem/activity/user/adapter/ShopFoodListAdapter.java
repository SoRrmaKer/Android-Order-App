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

import java.util.List;

public class ShopFoodListAdapter extends BaseAdapter {
    private Context context;
    private List<FoodBean> list;
    private LayoutInflater inflater;
    private OnCartUpdateListener cartListener;

    public interface OnCartUpdateListener {
        void onCartUpdated();
    }

    public void setOnCartUpdateListener(OnCartUpdateListener listener) {
        this.cartListener = listener;
    }

    public ShopFoodListAdapter(Context context, List<FoodBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<FoodBean> list) {
        this.list = list;
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
            convertView = inflater.inflate(R.layout.item_shop_food, parent, false);
            holder = new ViewHolder();
            holder.img = convertView.findViewById(R.id.shop_food_img);
            holder.name = convertView.findViewById(R.id.shop_food_name);
            holder.des = convertView.findViewById(R.id.shop_food_des);
            holder.sales = convertView.findViewById(R.id.shop_food_sales);
            holder.price = convertView.findViewById(R.id.shop_food_price);

            // 新增控件
            holder.btnAdd = convertView.findViewById(R.id.btn_add);
            holder.btnMinus = convertView.findViewById(R.id.btn_minus);
            holder.tvCount = convertView.findViewById(R.id.tv_count);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FoodBean bean = list.get(position);
        holder.name.setText(bean.getFoodName());
        holder.des.setText(bean.getFoodDes());
        holder.sales.setText("Sold: " + bean.getMonthSales());
        holder.price.setText("¥ " + bean.getFoodPrice());

        if (bean.getFoodImg() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(bean.getFoodImg());
            if (bitmap != null) holder.img.setImageBitmap(bitmap);
        }

        int count = CartManager.getFoodCount(bean.getFoodID());
        if (count > 0) {
            holder.btnMinus.setVisibility(View.VISIBLE);
            holder.tvCount.setVisibility(View.VISIBLE);
            holder.tvCount.setText(String.valueOf(count));
        } else {
            holder.btnMinus.setVisibility(View.GONE);
            holder.tvCount.setVisibility(View.GONE);
        }

        holder.btnAdd.setOnClickListener(v -> {
            CartManager.add(bean);
            notifyDataSetChanged();
            if (cartListener != null) cartListener.onCartUpdated();
        });

        holder.btnMinus.setOnClickListener(v -> {
            CartManager.reduce(bean);
            notifyDataSetChanged();
            if (cartListener != null) cartListener.onCartUpdated();
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView img, btnAdd, btnMinus;
        TextView name, des, sales, price, tvCount;
    }
}