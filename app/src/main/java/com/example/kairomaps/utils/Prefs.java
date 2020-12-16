package com.example.kairomaps.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Prefs {
    private static SharedPreferences preferences;
    private String name;
    public Prefs(Context context){
        preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public void putCoords(String KEY, List<LatLng> coords){
        Gson gson = new Gson();
        String json = gson.toJson(coords);
        preferences.edit().putString(KEY,json).apply();
    }
    public List<LatLng> getCoords(String KEY){
        List<LatLng> coords = new ArrayList<>();
        String converted = preferences.getString(KEY, null);
        if (converted != null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<LatLng>>(){}.getType();
            coords = gson.fromJson(converted, type);
        }
        return coords;
    }
    public void clearPrefs(){
        preferences.edit().clear().apply();
    }
}
