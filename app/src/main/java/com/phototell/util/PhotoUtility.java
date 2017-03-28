package com.phototell.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import com.phototell.PhotoTellApplication;
import com.phototell.data.Photo;
import java.io.ByteArrayOutputStream;

import static android.content.ContentValues.TAG;

/**
 * photo utility
 */
public class PhotoUtility {

    public static Photo convertUri(Context context, @NonNull Uri uri) {
        Log.d(TAG, "convertUri: " + uri.toString());
        try {
            Photo photo = new Photo();
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            String dateTaken = null;
            if (cursor != null) {
                cursor.moveToFirst();
                int column_index_date_taken = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
                dateTaken = cursor.getString(column_index_date_taken);
            }
            cursor.close();
            photo.setCreationDate(DateUtility.getDate(dateTaken));
            photo.setPath(uri.toString());
            AssetFileDescriptor fileDescriptor = context.getContentResolver().openAssetFileDescriptor(uri, "r");
            Bitmap bm = getThumbImage(uri);
            if (bm != null) {//TODO imrove
                photo.setThumbnailBytes(getBytes(bm));
                photo.setThumbnailBBitMap(bm);
            } else {//TODO imrove
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(PhotoTellApplication.getContext().getApplicationContext(), "there was an error uploading the image", duration);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
                toast.show();
                return null;
            }
            return photo;
        } catch (Exception e) {
            e.printStackTrace();//TODO fix ugly
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(PhotoTellApplication.getContext().getApplicationContext(), "there was an error uploading the image" + e.toString(), duration);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
            return null;
        }
    }

    private static Bitmap getThumbImage(Uri uri) {
        final int THUMBSIZE = 200;
        try {
            AssetFileDescriptor fileDescriptor = PhotoTellApplication.getContext().
                    getContentResolver().openAssetFileDescriptor(uri, "r");
            return ThumbnailUtils.extractThumbnail(getImage(fileDescriptor), THUMBSIZE, THUMBSIZE);

        } catch (Exception ex) {
            // Throwable ex;
            return null;
        }
    }

    private static Bitmap decodeBitmap(AssetFileDescriptor fileDescriptor) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        return BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
    }

    /**
     * Resize to avoid using too much memory loading big images (e.g.: 2560*1920)
     **/
    private static Bitmap getImage(AssetFileDescriptor fileDescriptor) {
        return decodeBitmap(fileDescriptor);
    }

    // convert from bitmap to byte array
    private static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(String path) {
        try {
            Uri uri = Uri.parse(path);
            AssetFileDescriptor fileDescriptor = PhotoTellApplication.getContext().getContentResolver().openAssetFileDescriptor(uri, "r");
            return getImage(fileDescriptor);
        } catch (Exception e) {
            //Do nothing
            return null;
        }
    }
}