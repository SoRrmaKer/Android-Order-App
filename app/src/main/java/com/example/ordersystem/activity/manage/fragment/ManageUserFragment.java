package com.example.ordersystem.activity.manage.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ordersystem.MainActivity;
import com.example.ordersystem.R;
import com.example.ordersystem.bean.BusinessBean;
import com.example.ordersystem.dao.AdminDao;

public class ManageUserFragment extends Fragment {

    private View rootView;
    private ImageView ivAvatar;
    private TextView tvShopName, tvShopDes, tvAccount;
    private TextView btnEdit, btnPwd, btnDel, btnLogout;
    private View orderManage, commentManage, historyManage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_manage_user, container, false);

        initView();
        initData();
        initListener();

        return rootView;
    }

    private void initView() {
        ivAvatar = rootView.findViewById(R.id.frag_user_avatar);
        tvShopName = rootView.findViewById(R.id.frag_user_shopname);
        tvShopDes = rootView.findViewById(R.id.frag_user_shopdes);
        tvAccount = rootView.findViewById(R.id.frag_user_account);

        btnEdit = rootView.findViewById(R.id.frag_user_edit_info);
        btnPwd = rootView.findViewById(R.id.frag_user_change_pwd);
        btnDel = rootView.findViewById(R.id.frag_user_delete_acc);
        btnLogout = rootView.findViewById(R.id.frag_user_logout);

        orderManage = rootView.findViewById(R.id.layout_order_manage);
        commentManage = rootView.findViewById(R.id.layout_comment_manage);
        historyManage = rootView.findViewById(R.id.layout_history_manage);
    }

    private void initData() {
        // 获取当前登录的 Merchant ID
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        String account = sharedPreferences.getString("account", "");

        BusinessBean business = AdminDao.getMerchantInfo(account);

        if (business != null) {
            tvAccount.setText("Account: " + business.getId());
            tvShopName.setText(business.getName());
            tvShopDes.setText("Intro: " + business.getDescribe());

            // 加载头像
            if (business.getImg() != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(business.getImg());
                if (bitmap != null) {
                    ivAvatar.setImageBitmap(bitmap);
                }
            }
        }
    }

    private void initListener() {
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), com.example.ordersystem.activity.manage.ManageMainEditInfoActivity.class);
            startActivity(intent);
        });

        btnPwd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), com.example.ordersystem.activity.manage.ManageMainPwdActivity.class);
            startActivity(intent);
        });

        btnDel.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Warning");
            builder.setMessage("Are you sure you want to DELETE your account? This Action cannot be Undone.");
            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences sp = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                    String account = sp.getString("account", "");
                    if(account.isEmpty()) account = sp.getString("account", "root");

                    int result = AdminDao.deleteMerchant(account);
                    if (result == 1) {
                        Toast.makeText(getContext(), "Account Deleted", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Failed to delete account", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        });

        btnLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Log Out");
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("Confirm", (dialog, which) -> {
                 SharedPreferences sp = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                 sp.edit().clear().apply();

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        });

        orderManage.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), com.example.ordersystem.activity.manage.ManageOrderActivity.class);
            startActivity(intent);
        });

        commentManage.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), com.example.ordersystem.activity.manage.ManageCommentActivity.class);
            startActivity(intent);
        });

        historyManage.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), com.example.ordersystem.activity.manage.ManageHistoryActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次回到页面刷新数据（以防在“修改信息”页面修改后返回）
        initData();
    }
}