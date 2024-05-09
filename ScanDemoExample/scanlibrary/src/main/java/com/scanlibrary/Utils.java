package com.scanlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jhansi on 05/04/15.
 */
public class Utils {
    static Date currentTime;
    private Utils() {

    }

    public static Uri getUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        Log.wtf("PATH", "before insertImage");
        // String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title" + " - " + (currentTime = Calendar.getInstance().getTime()), null);
        Log.wtf("PATH", path);
        return Uri.parse(path);
    }

    public static Bitmap getBitmap(Context context, Uri uri) throws IOException {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // Get just dimensions

        InputStream input = context.getContentResolver().openInputStream(uri);
        BitmapFactory.decodeStream(input, null, options);
        if (input != null) input.close();

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int desiredWidth = displayMetrics.widthPixels / 2;
        int desiredHeight = displayMetrics.heightPixels / 2;

        options.inSampleSize = calculateInSampleSize(options, desiredWidth, desiredHeight);

        // Calculate bitmap with new size
        options.inJustDecodeBounds = false;
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
        if (input != null) input.close();
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}