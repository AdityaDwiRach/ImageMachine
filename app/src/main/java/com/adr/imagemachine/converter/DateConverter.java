package com.adr.imagemachine.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DateConverter {

    @TypeConverter
    public static Date longToDate(Long data) {

        return data == null ? null : new Date(data);
    }

    @TypeConverter
    public static Long dateToLong(Date date) {
        return date == null ? null : date.getTime();
    }
}
