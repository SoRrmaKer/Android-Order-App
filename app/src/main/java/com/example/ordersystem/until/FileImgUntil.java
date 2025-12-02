package com.example.ordersystem.until;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.ordersystem.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileImgUntil {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static Future<Void> saveBitmapAsync(final Bitmap bitmap, final String path) {
        return executorService.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                saveImgBitmapToFile(bitmap, path);
                return null;
            }
        });
    }
    public static void saveImgBitmapToFile(Bitmap bitmap, String path) {

        File file = new File(path);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void saveImgBitmapToFile(Uri url, Context context, String path) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(url);

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            if (inputStream != null) {
                inputStream.close();
            }

            if (bitmap != null) {
                saveImgBitmapToFile(bitmap, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getPath() {
        String picName = "/" + UUID.randomUUID().toString().replace("-", "") + ".png";
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + picName;
    }

    public static void saveSystemImgToPath(Context context, int id, String path) {
        Drawable defaultDrawable = ContextCompat.getDrawable(context, id);
        if (defaultDrawable != null) {
            Bitmap bitmapDef = ((BitmapDrawable) defaultDrawable).getBitmap();
            FileImgUntil.saveBitmapAsync(bitmapDef, path);
        }
    }
}
