package com.adr.imagemachine.converter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class BitmapConverter {

    public byte[] bitmaptoByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        return stream.toByteArray();
    }

    public Bitmap byteArraytoBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}
