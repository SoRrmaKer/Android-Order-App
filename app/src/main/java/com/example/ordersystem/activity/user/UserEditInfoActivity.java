package com.example.ordersystem.activity.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.ordersystem.R;
import com.example.ordersystem.bean.UserBean;
import com.example.ordersystem.dao.UserDao;
import com.example.ordersystem.until.FileImgUntil;

public class UserEditInfoActivity extends AppCompatActivity {

    private ImageView ivAvatar;
    private EditText etName, etPhone, etAddress;
    private RadioButton rbMale, rbFemale;
    private String userId;
    private ActivityResultLauncher<String> getContentLauncher;
    private Uri uri;
    private UserBean currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_info);

        Toolbar toolbar = findViewById(R.id.edit_user_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        initView();

        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        userId = sp.getString("account", "");
        if (userId.isEmpty()) userId = sp.getString("account", "root");

        loadData();

        // 图片选择
        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                ivAvatar.setImageURI(result);
                uri = result;
            }
        });

        ivAvatar.setOnClickListener(v -> getContentLauncher.launch("image/*"));

        findViewById(R.id.edit_user_save_btn).setOnClickListener(v -> saveInfo());
    }

    private void initView() {
        ivAvatar = findViewById(R.id.edit_user_avatar);
        etName = findViewById(R.id.edit_user_name);
        etPhone = findViewById(R.id.edit_user_phone);
        etAddress = findViewById(R.id.edit_user_address);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
    }

    private void loadData() {
        currentUser = UserDao.getUserInfo(userId);
        if (currentUser != null) {
            etName.setText(currentUser.getName());
            etPhone.setText(currentUser.getPhone());
            etAddress.setText(currentUser.getAddress());

            if ("Male".equals(currentUser.getSex())) rbMale.setChecked(true);
            else rbFemale.setChecked(true);

            if (currentUser.getImg() != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(currentUser.getImg());
                if (bitmap != null) ivAvatar.setImageBitmap(bitmap);
            }
        }
    }

    private void saveInfo() {
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        String address = etAddress.getText().toString();
        String sex = rbMale.isChecked() ? "Male" : "Female";

        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser == null) {
            currentUser = UserDao.getUserInfo(userId);
            if (currentUser == null) {
                Toast.makeText(this, "Error: User data missing. Please login again.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String path = currentUser.getImg();

        if (ivAvatar.getDrawable() != null) {
            Drawable currentDrawable = ivAvatar.getDrawable();
            Bitmap currentBitmap = ((BitmapDrawable) currentDrawable).getBitmap();
            Drawable defaultDrawable = ContextCompat.getDrawable(this, R.drawable.uploadimg);
            Bitmap defaultBitmap = ((BitmapDrawable) defaultDrawable).getBitmap();

            if (!currentBitmap.sameAs(defaultBitmap) && uri != null) {
                path = FileImgUntil.getPath();
                FileImgUntil.saveImgBitmapToFile(uri, this, path);
            }
        }

        if (UserDao.updateUser(userId, name, sex, address, phone, path) == 1) {
            Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show();
            currentUser = UserDao.getUserInfo(userId);
            finish();
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}