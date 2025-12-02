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

import java.util.List;

public class UserFoodListAdapter extends BaseAdapter {
    private Context context;
    private List<FoodBean> list;
    private LayoutInflater inflater;

    public UserFoodListAdapter(Context context, List<FoodBean> list) {
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
            convertView = inflater.inflate(R.layout.item_user_home_food, parent, false);
            holder = new ViewHolder();
            holder.ivFood = convertView.findViewById(R.id.u_item_food_img);
            holder.tvName = convertView.findViewById(R.id.u_item_food_name);
            holder.tvSales = convertView.findViewById(R.id.u_item_month_sold);
            holder.tvPrice = convertView.findViewById(R.id.u_item_food_price);
            holder.tvDes = convertView.findViewById(R.id.u_item_food_des);
            holder.ivMerchant = convertView.findViewById(R.id.u_item_merchant_avatar);
            holder.tvMerchantName = convertView.findViewById(R.id.u_item_merchant_name);
            holder.tvRating = convertView.findViewById(R.id.u_item_rating);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FoodBean food = list.get(position);

        holder.tvName.setText(food.getFoodName());
        holder.tvSales.setText("Month Sold: " + food.getMonthSales());
        holder.tvPrice.setText(food.getFoodPrice());
        holder.tvDes.setText(food.getFoodDes());
        holder.tvMerchantName.setText(food.getMerchantName());
        holder.tvRating.setText(food.getRating());

        if (food.getFoodImg() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(food.getFoodImg());
            if(bitmap != null) holder.ivFood.setImageBitmap(bitmap);
        }

        if (food.getMerchantAvatar() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(food.getMerchantAvatar());
            if(bitmap != null) holder.ivMerchant.setImageBitmap(bitmap);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView ivFood, ivMerchant;
        TextView tvName, tvSales, tvPrice, tvDes, tvMerchantName, tvRating;
    }
}