package com.example.ordersystem.activity.manage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.ordersystem.R;
import com.example.ordersystem.dao.FoodDao;
import com.example.ordersystem.until.FileImgUntil;

public class ManageMainAddFoodActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> getContentLauncher;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_man_add_food_acitvity);

        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String merchantId = sharedPreferences.getString("account", "root");

        ImageView img = findViewById(R.id.main_manage_addFood_img);

        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result != null) {
                    img.setImageURI(result);
                    uri = result;
                } else {
                    Toast.makeText(ManageMainAddFoodActivity.this, "Please Choose Avatar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(v);
            }
        });


        EditText nameText = findViewById(R.id.main_manage_addFood_name);
        EditText priceText = findViewById(R.id.main_manage_addFood_price);
        EditText desText = findViewById(R.id.main_manage_addFood_des);
        Button btnAddFood = findViewById(R.id.main_manage_addFood_addBtn);

        Drawable defaultDrawable = ContextCompat.getDrawable(this, R.drawable.uploadimg);

        Toolbar toolbar = this.findViewById(R.id.main_manage_addFood_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String price = priceText.getText().toString();
                String des = desText.getText().toString();
                if(name.isEmpty()) {
                    Toast.makeText(ManageMainAddFoodActivity.this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();
                } else if (price.isEmpty()) {
                    Toast.makeText(ManageMainAddFoodActivity.this, "Please Enter Product Price", Toast.LENGTH_SHORT).show();
                } else if (des.isEmpty()) {
                    Toast.makeText(ManageMainAddFoodActivity.this, "Please Enter Product Description", Toast.LENGTH_SHORT).show();
                } else {
                    Drawable drawable = img.getDrawable();
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    Bitmap defbitmap = ((BitmapDrawable) defaultDrawable).getBitmap();
                    if(bitmap.sameAs(defbitmap)) {
                        Toast.makeText(ManageMainAddFoodActivity.this, "Please Choose Food Image", Toast.LENGTH_SHORT).show();
                    } else {
                        String path = FileImgUntil.getPath();
                        FileImgUntil.saveImgBitmapToFile(uri, ManageMainAddFoodActivity.this, path);
                        int a = FoodDao.addFood(merchantId, name, des, price, path);
                        if (a == 1) {
                            Toast.makeText(ManageMainAddFoodActivity.this, "Succeed", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ManageMainAddFoodActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }

    private void openGallery(View v) {
        getContentLauncher.launch("image/*");
    }
}