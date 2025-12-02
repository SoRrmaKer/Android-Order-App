package com.example.ordersystem.activity.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.ordersystem.R;
import com.example.ordersystem.bean.ReviewBean;
import com.example.ordersystem.dao.ReviewDao;
import com.example.ordersystem.until.FileImgUntil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UserAddReviewActivity extends AppCompatActivity {

    private String orderId;
    private String merchantId;
    private String userId;

    private ImageView ivImg;
    private EditText etContent;
    private RatingBar ratingBar;

    private ActivityResultLauncher<String> getContentLauncher;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_review);

        orderId = getIntent().getStringExtra("orderId");
        merchantId = getIntent().getStringExtra("merchantId");

        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        userId = sp.getString("account", "");
        if (userId.isEmpty()) userId = sp.getString("account", "root");

        Toolbar toolbar = findViewById(R.id.review_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        initView();

        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                ivImg.setImageURI(result);
                uri = result;
            }
        });

        ivImg.setOnClickListener(v -> getContentLauncher.launch("image/*"));

        findViewById(R.id.btn_submit_review).setOnClickListener(v -> submitReview());
    }

    private void initView() {
        ivImg = findViewById(R.id.review_img_preview);
        etContent = findViewById(R.id.review_content);
        ratingBar = findViewById(R.id.review_rating_bar);
    }

    private void submitReview() {
        String content = etContent.getText().toString();
        float rating = ratingBar.getRating();

        if (content.isEmpty()) {
            Toast.makeText(this, "Please write something...", Toast.LENGTH_SHORT).show();
            return;
        }

        String imgPath = "";
        if (uri != null) {
            imgPath = FileImgUntil.getPath();
            FileImgUntil.saveImgBitmapToFile(uri, this, imgPath);
        }

        ReviewBean review = new ReviewBean();
        review.setReviewId(UUID.randomUUID().toString());
        review.setOrderId(orderId);
        review.setUserId(userId);
        review.setMerchantId(merchantId);
        review.setRating(rating);
        review.setContent(content);
        review.setImgPath(imgPath);
        review.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        if (ReviewDao.addReview(review)) {
            Toast.makeText(this, "Review Published!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to publish review", Toast.LENGTH_SHORT).show();
        }
    }
}