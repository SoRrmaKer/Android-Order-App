package com.example.ordersystem.activity.manage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ordersystem.R;
import com.example.ordersystem.bean.ReviewBean;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private List<ReviewBean> list;
    private LayoutInflater inflater;

    public CommentAdapter(Context context, List<ReviewBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.item_manage_comment, parent, false);
            holder = new ViewHolder();
            holder.ivAvatar = convertView.findViewById(R.id.item_comment_avatar);
            holder.tvName = convertView.findViewById(R.id.item_comment_name);
            holder.tvTime = convertView.findViewById(R.id.item_comment_time);
            holder.ratingBar = convertView.findViewById(R.id.item_comment_rating);
            holder.tvContent = convertView.findViewById(R.id.item_comment_content);
            holder.ivCommentImg = convertView.findViewById(R.id.item_comment_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ReviewBean bean = list.get(position);

        holder.tvName.setText(bean.getUserName());
        holder.tvTime.setText(bean.getTime());
        holder.tvContent.setText(bean.getContent());
        holder.ratingBar.setRating(bean.getRating());

        // 加载头像
        if (bean.getUserAvatar() != null && !bean.getUserAvatar().isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(bean.getUserAvatar());
            if (bitmap != null) holder.ivAvatar.setImageBitmap(bitmap);
        }

        // 加载评论图片 (如果存在)
        if (bean.getImgPath() != null && !bean.getImgPath().isEmpty()) {
            holder.ivCommentImg.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(bean.getImgPath());
            if (bitmap != null) {
                holder.ivCommentImg.setImageBitmap(bitmap);
            }
        } else {
            holder.ivCommentImg.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView ivAvatar, ivCommentImg;
        TextView tvName, tvTime, tvContent;
        RatingBar ratingBar;
    }
}