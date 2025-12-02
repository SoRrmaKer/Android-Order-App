package com.example.ordersystem.activity.manage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ordersystem.R;
import com.example.ordersystem.activity.manage.ManageMainUpdateFoodActivity;
import com.example.ordersystem.bean.FoodBean;
import com.example.ordersystem.dao.FoodDao;

import java.util.List;

public class FoodListAdapter extends ArrayAdapter<FoodBean> {

    private List<FoodBean> list;

    private Context context;
    public FoodListAdapter(@NonNull Context context, List<FoodBean> list) {
        super(context, R.layout.man_foodlist, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.man_foodlist, viewGroup, false);
        }

        FoodBean temp = list.get(position);

        ImageView img = convertView.findViewById(R.id.man_foodlist_foodImg);
        TextView name = convertView.findViewById(R.id.man_foodlist_name);
        TextView sale =  convertView.findViewById(R.id.man_foodlist_sale);
        TextView price =  convertView.findViewById(R.id.man_foodlist_price);
        TextView des =  convertView.findViewById(R.id.man_foodlist_des);
//        convertView.findViewById(R.id.man_foodlist_foodImg);

        Bitmap bitmap = BitmapFactory.decodeFile(temp.getFoodImg());
        img.setImageBitmap(bitmap);
        name.setText(temp.getFoodName());
        price.setText(temp.getFoodPrice());
        des.setText(temp.getFoodDes());

        int saleNum = FoodDao.getMonthSale(temp.getFoodID());
        sale.setText("Month Sale: " + String.valueOf(saleNum));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ManageMainUpdateFoodActivity.class);
                intent.putExtra("food", temp);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
