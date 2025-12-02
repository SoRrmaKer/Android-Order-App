package com.example.ordersystem.activity.manage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ordersystem.R;
import com.example.ordersystem.bean.FoodBean;
import com.example.ordersystem.dao.FoodDao;
import com.example.ordersystem.until.FileImgUntil;

public class ManageMainUpdateFoodActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> getContentLauncher;

    Uri uri;

    String foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_main_update_food);

        Intent intent = getIntent();
        FoodBean food = (FoodBean) intent.getSerializableExtra("food");
        foodId = food.getFoodID();

        ImageView img = findViewById(R.id.main_manage_updateFood_img);
        Bitmap bitmap = BitmapFactory.decodeFile(food.getFoodImg());
        img.setImageBitmap(bitmap);

        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result != null) {
                    img.setImageURI(result);
                    uri = result;
                } else {
                    Toast.makeText(ManageMainUpdateFoodActivity.this, "Please Choose Avatar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(v);
            }
        });


        EditText nameText = findViewById(R.id.main_manage_updateFood_name);
        nameText.setText(food.getFoodName());
        EditText priceText = findViewById(R.id.main_manage_updateFood_price);
        priceText.setText(food.getFoodPrice());
        EditText desText = findViewById(R.id.main_manage_updateFood_des);
        desText.setText(food.getFoodDes());
        Button btnUpdateFood = findViewById(R.id.main_manage_updateFood_updateBtn);

        Drawable drawable = img.getDrawable();
        Bitmap defaultDrawable = ((BitmapDrawable) drawable).getBitmap();

        Toolbar toolbar = this.findViewById(R.id.main_manage_updateFood_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdateFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String price = priceText.getText().toString();
                String des = desText.getText().toString();
                if(name.isEmpty()) {
                    Toast.makeText(ManageMainUpdateFoodActivity.this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();
                } else if (price.isEmpty()) {
                    Toast.makeText(ManageMainUpdateFoodActivity.this, "Please Enter Product Price", Toast.LENGTH_SHORT).show();
                } else if (des.isEmpty()) {
                    Toast.makeText(ManageMainUpdateFoodActivity.this, "Please Enter Product Description", Toast.LENGTH_SHORT).show();
                } else {
                    String path = food.getFoodImg();

                    if (uri != null) {
                        path = FileImgUntil.getPath();
                        FileImgUntil.saveImgBitmapToFile(uri, ManageMainUpdateFoodActivity.this, path);
                    }

                    int a = FoodDao.updateFood(food.getFoodID(), name, des, price, path);
                    if (a == 1) {
                        Toast.makeText(ManageMainUpdateFoodActivity.this, "Succeed", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ManageMainUpdateFoodActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void openGallery(View v) {
        getContentLauncher.launch("image/*");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.man_manage_food_del, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int a = item.getItemId();
        if (a == R.id.main_manage_bottom_menu_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Item");
            builder.setMessage("Confirm Delete this Item?");
            builder.setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int a = FoodDao.delFood(foodId);
                    if(a == 1) {
                        Toast.makeText(ManageMainUpdateFoodActivity.this, "Succeed", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ManageMainUpdateFoodActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}