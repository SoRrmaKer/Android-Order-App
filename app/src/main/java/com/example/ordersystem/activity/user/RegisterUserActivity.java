package com.example.ordersystem.activity.user;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ordersystem.R;
import com.example.ordersystem.dao.AdminDao;
import com.example.ordersystem.until.FileImgUntil;

import java.util.UUID;

public class RegisterUserActivity extends AppCompatActivity {
    private ActivityResultLauncher<String> getContentLauncher;

    Uri uri;

    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_user);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        Toolbar back = findViewById(R.id.user_back);
        setSupportActionBar(back);

        back.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView avatar = findViewById(R.id.user_avatar);

        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result != null) {
                    avatar.setImageURI(result);
                    uri = result;
                } else {
                    Toast.makeText(RegisterUserActivity.this, "Please Choose Avatar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Find All Id
        EditText idText = findViewById(R.id.user_account);
        EditText pwdText = findViewById(R.id.user_pwd);
        EditText nameText = findViewById(R.id.user_name);

        sex = "Female";
        RadioButton male = findViewById(R.id.user_male);
        male.setChecked(true);
        if(male.isChecked()) {
            sex = "Male";
        }

        EditText addressText = findViewById(R.id.user_address);
        EditText phoneText = findViewById(R.id.user_phone);

        Drawable defaultDrawable = ContextCompat.getDrawable(this, R.drawable.uploadimg);

        Button registerBtn = findViewById(R.id.btn_user);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idText.getText().toString();
                String pwd = pwdText.getText().toString();
                String name = nameText.getText().toString();
                String address = addressText.getText().toString();
                String phone = phoneText.getText().toString();


                Drawable drawable = avatar.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Bitmap defbitmap = ((BitmapDrawable) defaultDrawable).getBitmap();
                if(bitmap.sameAs(defbitmap)) {
                    Toast.makeText(RegisterUserActivity.this, "Please Choose Avatar", Toast.LENGTH_SHORT).show();
                } else if (id.isEmpty()) {
                    Toast.makeText(RegisterUserActivity.this, "Please Enter Account", Toast.LENGTH_SHORT).show();
                } else if (pwd.isEmpty()) {
                    Toast.makeText(RegisterUserActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()) {
                    Toast.makeText(RegisterUserActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else if (address.isEmpty()) {
                    Toast.makeText(RegisterUserActivity.this, "Please Enter Address", Toast.LENGTH_SHORT).show();
                } else if (phone.isEmpty()) {
                    Toast.makeText(RegisterUserActivity.this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                } else {
                    String path = FileImgUntil.getPath();
                    FileImgUntil.saveImgBitmapToFile(uri, RegisterUserActivity.this, path);
                    int a = AdminDao.saveUser(id, pwd, name, sex, address, phone, path);
                    if(a == 1) {
                        Toast.makeText(RegisterUserActivity.this, "Sign Up Succeed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterUserActivity.this, "Failed to Sign Up", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(v);
            }
        });
    }

    private void openGallery(View v) {
        getContentLauncher.launch("image/*");
    }
}