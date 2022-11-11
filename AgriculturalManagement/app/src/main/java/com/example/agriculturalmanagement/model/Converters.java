package com.example.agriculturalmanagement.model;

import androidx.room.TypeConverter;

import java.time.Duration;
import java.util.Date;

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
}
