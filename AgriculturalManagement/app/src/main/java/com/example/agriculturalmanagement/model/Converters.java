package com.example.agriculturalmanagement.model;

import androidx.room.TypeConverter;

import com.example.agriculturalmanagement.model.entities.ComplexArea;
import com.example.agriculturalmanagement.model.entities.GenArea;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Date;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Long Date2Long(Date d) {
        return d != null ? d.getTime() : null;
    }

    @TypeConverter
    public static Date Long2Date(Long l) {
        return l != null ? new Date(l) : null;
    }

    @TypeConverter
    public static Long Duration2Long(Duration d) {
        return d != null ? d.toNanos() : null;
    }

    @TypeConverter
    public static Duration Long2Duration(Long l) {
        return l != null ? Duration.ofNanos(l) : null;
    }


    // TODO
    // be aware of polymophism
    // in this version there will be information loss
    @TypeConverter
    // using serialization
    public static String GenArea2String(GenArea g) { return g.toString(); }

    @TypeConverter
    // using deserialization
    public static GenArea StringToGenArea(String s) {

        GenArea g = new GenArea();
        g.fromString(s);
        return g;
    }

    @TypeConverter
    // using serialization
    public static String GenAreaKey2String(GenAreaKey g) { return GenAreaKey.toString(g); }

    @TypeConverter
    // using deserialization
    public static GenAreaKey String2GenAreaKey(String s) { return GenAreaKey.fromString(s); }

    @TypeConverter
    // using serialization
    public static String ComplexArea2String(ComplexArea c) { return c.toString(); }

    @TypeConverter
    // using deserialization
    public static ComplexArea String2ComplexArea(String s) throws Exception {

        ComplexArea c = new ComplexArea();
        c.fromString(s);

        return c;
    }

    @TypeConverter
    public static String ListGenArea2String(List<GenArea> l) {

        Gson gson = new Gson();
        return gson.toJson(l);
    }

    @TypeConverter
    public static List<GenArea> String2ListGenArea(String s) throws JSONException {

        Gson gson = new Gson();
        Type type = new TypeToken<List<GenArea> >() { }.getType();
        return gson.fromJson(s, type);
    }
}
