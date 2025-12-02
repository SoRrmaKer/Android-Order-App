package com.example.ordersystem.activity.user.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ordersystem.MainActivity;
import com.example.ordersystem.R;
import com.example.ordersystem.bean.UserBean;
import com.example.ordersystem.dao.UserDao;
import com.example.ordersystem.activity.user.UserHistoryOrderActivity;
import com.example.ordersystem.activity.user.UserEditInfoActivity;
import com.example.ordersystem.activity.user.UserChangePwdActivity;

public class UserProfileFragment extends Fragment {

    private ImageView ivAvatar;
    private TextView tvName, tvAccount;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        ivAvatar = view.findViewById(R.id.u_profile_avatar);
        tvName = view.findViewById(R.id.u_profile_name);
        tvAccount = view.findViewById(R.id.u_profile_account);

        // 获取 User ID
        SharedPreferences sp = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        userId = sp.getString("account", "");
        if (userId.isEmpty()) userId = sp.getString("account", "root");

        // 按钮事件
        view.findViewById(R.id.btn_u_history).setOnClickListener(v -> startActivity(new Intent(getContext(), UserHistoryOrderActivity.class)));
        view.findViewById(R.id.btn_u_edit_info).setOnClickListener(v -> startActivity(new Intent(getContext(), UserEditInfoActivity.class)));
        view.findViewById(R.id.btn_u_change_pwd).setOnClickListener(v -> startActivity(new Intent(getContext(), UserChangePwdActivity.class)));

        view.findViewById(R.id.btn_u_delete_acc).setOnClickListener(v -> showDeleteDialog());
        view.findViewById(R.id.btn_u_logout).setOnClickListener(v -> showLogoutDialog());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        UserBean user = UserDao.getUserInfo(userId);
        if (user != null) {
            tvName.setText(user.getName());
            tvAccount.setText("Account: " + user.getId());
            if (user.getImg() != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(user.getImg());
                if (bitmap != null) ivAvatar.setImageBitmap(bitmap);
            }
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Log Out")
                .setMessage("Are you sure?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Warning")
                .setMessage("Delete Account? This cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (UserDao.deleteUser(userId) == 1) {
                        Toast.makeText(getContext(), "Account Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}