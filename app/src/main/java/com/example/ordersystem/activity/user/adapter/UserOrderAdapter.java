package com.example.ordersystem.activity.user.adapter;

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

public class UserOrderAdapter extends BaseAdapter {
    private Context context;
    private List<OrderBean> list;
    private LayoutInflater inflater;

    public UserOrderAdapter(Context context, List<OrderBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

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
            convertView = inflater.inflate(R.layout.item_user_order, parent, false);
            holder = new ViewHolder();
            holder.ivAvatar = convertView.findViewById(R.id.u_order_merchant_avatar);
            holder.tvShopName = convertView.findViewById(R.id.u_order_merchant_name);
            holder.tvStatus = convertView.findViewById(R.id.u_order_status);
            holder.tvTime = convertView.findViewById(R.id.u_order_time);
            holder.itemsContainer = convertView.findViewById(R.id.u_order_items_container);
            holder.tvAddress = convertView.findViewById(R.id.u_order_address);
            holder.tvTotal = convertView.findViewById(R.id.u_order_total);
            holder.actionLayout = convertView.findViewById(R.id.u_order_action_layout);
            holder.btnCancel = convertView.findViewById(R.id.btn_u_cancel);
            holder.btnConfirm = convertView.findViewById(R.id.btn_u_confirm);
            holder.btnReview = convertView.findViewById(R.id.btn_u_review);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderBean order = list.get(position);

        holder.tvShopName.setText(order.getMerchantName());
        if (order.getMerchantAvatar() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(order.getMerchantAvatar());
            if(bitmap != null) holder.ivAvatar.setImageBitmap(bitmap);
        }

        holder.tvTime.setText(order.getOrderTime());
        holder.tvAddress.setText(order.getAddress());
        holder.tvTotal.setText("¥ " + order.getTotalPrice());

        if (order.getStatus() == 0) {
            holder.tvStatus.setText("Pending");
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800"));
            holder.actionLayout.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.btnConfirm.setVisibility(View.VISIBLE);
            holder.btnReview.setVisibility(View.GONE);

        } else if (order.getStatus() == 1) {
            holder.tvStatus.setText("Completed");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));

            if (!order.isReviewed()) {
                holder.actionLayout.setVisibility(View.VISIBLE);
                holder.btnCancel.setVisibility(View.GONE);
                holder.btnConfirm.setVisibility(View.GONE);
                holder.btnReview.setVisibility(View.VISIBLE);
            } else {
                holder.actionLayout.setVisibility(View.GONE);
            }

        } else {
            holder.tvStatus.setText("Cancelled");
            holder.tvStatus.setTextColor(Color.parseColor("#9E9E9E"));
            holder.actionLayout.setVisibility(View.GONE);
        }

        holder.itemsContainer.removeAllViews();
        if (order.getDetailsList() != null) {
            for (OrderDetailBean detail : order.getDetailsList()) {
                View itemView = inflater.inflate(R.layout.item_manage_order_food, null);
                ImageView img = itemView.findViewById(R.id.item_food_img);
                TextView name = itemView.findViewById(R.id.item_food_name);
                TextView count = itemView.findViewById(R.id.item_food_count);
                TextView price = itemView.findViewById(R.id.item_food_price);

                name.setText(detail.getFoodName());
                count.setText("x " + detail.getCount());
                price.setText("¥ " + detail.getPrice());

                if (detail.getImg() != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(detail.getImg());
                    if (bitmap != null) img.setImageBitmap(bitmap);
                }
                holder.itemsContainer.addView(itemView);
            }
        }

        holder.btnCancel.setOnClickListener(v -> updateOrderStatus(order.getOrderId(), 2, position));
        holder.btnConfirm.setOnClickListener(v -> updateOrderStatus(order.getOrderId(), 1, position));
        holder.btnReview.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(context, com.example.ordersystem.activity.user.UserAddReviewActivity.class);
            intent.putExtra("orderId", order.getOrderId());
            intent.putExtra("merchantId", order.getMerchantId()); // 需要传商家ID关联
            context.startActivity(intent);
        });

        return convertView;
    }

    private void updateOrderStatus(String orderId, int status, int position) {
        String msg = status == 2 ? "Cancel this order?" : "Confirm receipt?";
        new AlertDialog.Builder(context)
                .setTitle("Confirm")
                .setMessage(msg)
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (OrderDao.updateOrderStatus(orderId, status) == 1) {
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

    static class ViewHolder {
        ImageView ivAvatar;
        TextView tvShopName, tvStatus, tvTime, tvAddress, tvTotal;
        LinearLayout itemsContainer, actionLayout;
        Button btnCancel, btnConfirm, btnReview;
    }
}