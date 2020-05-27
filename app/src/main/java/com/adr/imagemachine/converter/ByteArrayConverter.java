package com.adr.imagemachine.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ByteArrayConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<byte[]> stringToListByteArray(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<byte[]>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String listByteArrayToString(List<byte[]> listImage) {
        return gson.toJson(listImage);
    }
}
