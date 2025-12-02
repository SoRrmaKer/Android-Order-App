package com.example.ordersystem.activity.manage.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.ordersystem.R;
import com.example.ordersystem.bean.OrderBean;
import com.example.ordersystem.bean.OrderDetailBean;
import com.example.ordersystem.dao.OrderDao;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<OrderBean> list;
    private LayoutInflater inflater;

    public OrderAdapter(Context context, List<OrderBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    // 更新列表数据
    public void setList(List<OrderBean> list) {
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
            convertView = inflater.inflate(R.layout.item_manage_order, parent, false);
            holder = new ViewHolder();
            holder.ivAvatar = convertView.findViewById(R.id.item_order_avatar);
            holder.tvUsername = convertView.findViewById(R.id.item_order_username);
            holder.tvTime = convertView.findViewById(R.id.item_order_time);
            holder.tvRecipient = convertView.findViewById(R.id.item_order_recipient);
            holder.tvAddress = convertView.findViewById(R.id.item_order_address);
            holder.tvPhone = convertView.findViewById(R.id.item_order_phone);
            holder.llFoodContainer = convertView.findViewById(R.id.ll_food_container);
            holder.tvStatus = convertView.findViewById(R.id.item_order_status);
            holder.tvTotal = convertView.findViewById(R.id.item_order_total);
            holder.btnCancel = convertView.findViewById(R.id.btn_cancel_order);
            holder.btnComplete = convertView.findViewById(R.id.btn_complete_order);
            holder.llActions = convertView.findViewById(R.id.ll_order_actions);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderBean order = list.get(position);

        // 1. 设置基本信息
        holder.tvUsername.setText(order.getUserName());
        holder.tvTime.setText(order.getOrderTime());
        holder.tvRecipient.setText("Recipient: " + order.getUserName()); // 暂时用用户名
        holder.tvAddress.setText("Address: " + order.getAddress());
        holder.tvPhone.setText("Tel: " + order.getUserPhone());
        holder.tvTotal.setText("¥ " + order.getTotalPrice());

        if (order.getUserAvatar() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(order.getUserAvatar());
            if(bitmap!=null) holder.ivAvatar.setImageBitmap(bitmap);
        }

        // 2. 设置状态与按钮逻辑
        // Status: 0-Pending, 1-Completed, 2-Cancelled
        if (order.getStatus() == 0) {
            holder.tvStatus.setText("Pending Process");
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800")); // Orange
            holder.llActions.setVisibility(View.VISIBLE); // 显示操作按钮
        } else if (order.getStatus() == 1) {
            holder.tvStatus.setText("Completed");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
            holder.llActions.setVisibility(View.GONE); // 隐藏按钮
        } else {
            holder.tvStatus.setText("Cancelled");
            holder.tvStatus.setTextColor(Color.parseColor("#9E9E9E")); // Grey
            holder.llActions.setVisibility(View.GONE);
        }

        // 3. 动态加载商品列表 (关键点)
        holder.llFoodContainer.removeAllViews(); // 清空旧数据
        if (order.getDetailsList() != null) {
            for (OrderDetailBean food : order.getDetailsList()) {
                View foodView = inflater.inflate(R.layout.item_manage_order_food, null);
                ImageView ivFood = foodView.findViewById(R.id.item_food_img);
                TextView tvName = foodView.findViewById(R.id.item_food_name);
                TextView tvCount = foodView.findViewById(R.id.item_food_count);
                TextView tvPrice = foodView.findViewById(R.id.item_food_price);

                tvName.setText(food.getFoodName());
                tvCount.setText("x " + food.getCount());
                tvPrice.setText("¥ " + food.getPrice());

                if(food.getImg()!=null){
                    Bitmap bitmap = BitmapFactory.decodeFile(food.getImg());
                    if(bitmap!=null) ivFood.setImageBitmap(bitmap);
                }

                holder.llFoodContainer.addView(foodView);
            }
        }

        holder.btnCancel.setOnClickListener(v -> showConfirmDialog(order.getOrderId(), 2, "Cancel Order?", position));
        holder.btnComplete.setOnClickListener(v -> showConfirmDialog(order.getOrderId(), 1, "Complete Order?", position));

        return convertView;
    }
    private void showConfirmDialog(String orderId, int status, String msg, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Confirm")
                .setMessage(msg)
                .setPositiveButton("Yes", (dialog, which) -> {
                    int res = OrderDao.updateOrderStatus(orderId, status);
                    if (res == 1) {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        list.remove(position);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void updateStatusInList(String orderId, int status) {
        for (OrderBean bean : list) {
            if (bean.getOrderId().equals(orderId)) {
                bean.setStatus(status);
                break;
            }
        }
    }

    static class ViewHolder {
        ImageView ivAvatar;
        TextView tvUsername, tvTime, tvRecipient, tvAddress, tvPhone, tvStatus, tvTotal;
        LinearLayout llFoodContainer, llActions;
        Button btnCancel, btnComplete;
    }
}