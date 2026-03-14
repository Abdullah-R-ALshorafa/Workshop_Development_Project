package com.example.workshop_development_project.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class Converters {

    @TypeConverter
    public long toLong(Date date) {
        return date.getTime();
    }
    @TypeConverter
    public Date toDate(long date) {
        return new Date(date);
    }
    @TypeConverter
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        if (bitmap == null) return null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @TypeConverter
    public static Bitmap getByteArrayAsBitmap(byte[] bytes) {
        if (bytes == null) return null;

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}

