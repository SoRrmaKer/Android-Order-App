package com.example.ordersystem.activity.manage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.ordersystem.R;
import com.example.ordersystem.bean.BusinessBean;
import com.example.ordersystem.dao.AdminDao;
import com.example.ordersystem.until.FileImgUntil;

public class ManageMainEditInfoActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> getContentLauncher;
    private Uri uri;
    private String merchantId;
    private BusinessBean currentBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_edit_info);

        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        merchantId = sp.getString("account", "");
        if(merchantId.isEmpty()) merchantId = sp.getString("account", "root"); // 兼容

        Toolbar toolbar = findViewById(R.id.edit_info_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ImageView ivImg = findViewById(R.id.edit_info_img);
        EditText etName = findViewById(R.id.edit_info_name);
        EditText etType = findViewById(R.id.edit_info_type);
        EditText etDes = findViewById(R.id.edit_info_des);
        Button btnSave = findViewById(R.id.edit_info_btn);

        currentBusiness = AdminDao.getMerchantInfo(merchantId);
        if (currentBusiness != null) {
            etName.setText(currentBusiness.getName());
            etType.setText(currentBusiness.getType());
            etDes.setText(currentBusiness.getDescribe());
            if (currentBusiness.getImg() != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(currentBusiness.getImg());
                if(bitmap != null) ivImg.setImageBitmap(bitmap);
            }
        }

        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                ivImg.setImageURI(result);
                uri = result;
            }
        });
        ivImg.setOnClickListener(v -> getContentLauncher.launch("image/*"));

        Drawable defaultDrawable = ContextCompat.getDrawable(this, R.drawable.uploadimg);
        Bitmap defaultBitmap = ((BitmapDrawable) defaultDrawable).getBitmap();

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String type = etType.getText().toString();
            String des = etDes.getText().toString();

            if (name.isEmpty() || type.isEmpty()) {
                Toast.makeText(this, "Name and Type cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            String path = currentBusiness.getImg();
            Drawable currentDrawable = ivImg.getDrawable();
            Bitmap currentBitmap = ((BitmapDrawable) currentDrawable).getBitmap();

            if (!currentBitmap.sameAs(defaultBitmap) && uri != null) {
                path = FileImgUntil.getPath(); // 生成新路径
                FileImgUntil.saveImgBitmapToFile(uri, this, path);
            }

            int result = AdminDao.updateMerchant(merchantId, name, des, type, path);
            if (result == 1) {
                Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}