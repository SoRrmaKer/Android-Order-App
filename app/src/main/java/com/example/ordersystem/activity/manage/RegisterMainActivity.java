package com.example.ordersystem.activity.manage;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.ordersystem.R;
import com.example.ordersystem.dao.AdminDao;
import com.example.ordersystem.until.FileImgUntil;

public class RegisterMainActivity extends AppCompatActivity {
    private ActivityResultLauncher<String> getContentLauncher;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_man);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        Toolbar back = findViewById(R.id.register_back);
        setSupportActionBar(back);

        back.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView avatar = findViewById(R.id.register_img);

        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result != null) {
                    avatar.setImageURI(result);
                    uri = result;
                } else {
                    Toast.makeText(RegisterMainActivity.this, "Please Choose Avatar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Find All Id
        EditText idText = findViewById(R.id.signup_account);
        EditText pwdText = findViewById(R.id.signup_pwd);
        EditText desText = findViewById(R.id.signup_des);
        EditText nameText = findViewById(R.id.signup_name);
        EditText typeText = findViewById(R.id.signup_type);

        Drawable defaultDrawable = ContextCompat.getDrawable(this, R.drawable.uploadimg);

        Button registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idText.getText().toString();
                String pwd = pwdText.getText().toString();
                String des = desText.getText().toString();
                String name = nameText.getText().toString();
                String type = typeText.getText().toString();


                Drawable drawable = avatar.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Bitmap defbitmap = ((BitmapDrawable) defaultDrawable).getBitmap();
                if(bitmap.sameAs(defbitmap)) {
                    Toast.makeText(RegisterMainActivity.this, "Please Choose Avatar", Toast.LENGTH_SHORT).show();
                } else if (id.isEmpty()) {
                    Toast.makeText(RegisterMainActivity.this, "Please Enter Account", Toast.LENGTH_SHORT).show();
                } else if (pwd.isEmpty()) {
                    Toast.makeText(RegisterMainActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else if (des.isEmpty()) {
                    Toast.makeText(RegisterMainActivity.this, "Please Enter Description", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()) {
                    Toast.makeText(RegisterMainActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else if (type.isEmpty()) {
                    Toast.makeText(RegisterMainActivity.this, "Please Enter Type", Toast.LENGTH_SHORT).show();
                } else {
                    String path = FileImgUntil.getPath();
                    FileImgUntil.saveImgBitmapToFile(uri, RegisterMainActivity.this, path);
                    int a = AdminDao.saveMerchant(id, pwd, des, name, type, path);
                    if(a == 1) {
                        Toast.makeText(RegisterMainActivity.this, "Sign Up Succeed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterMainActivity.this, "Failed to Sign Up", Toast.LENGTH_SHORT).show();
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