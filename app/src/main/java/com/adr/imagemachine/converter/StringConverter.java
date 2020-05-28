package com.adr.imagemachine.converter;

import android.net.Uri;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class StringConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<String> stringToListUri(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String listUriToString(List<String> listImage) {
        return gson.toJson(listImage);
    }
}
