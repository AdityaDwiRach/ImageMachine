package com.adr.imagemachine.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DateConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static Date stringToDate(String data) {
        if (data == null) {
            return new Date();
        }

        Type listType = new TypeToken<Date>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String dateToString(Date date) {
        return gson.toJson(date);
    }
}
