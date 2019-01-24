package com.tma.android.contapp.data;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArrayListConverter {

    @TypeConverter
    public static ArrayList<Produs> fromProdus(String value) {
        Type listType = new TypeToken<ArrayList<Produs>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Produs> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
