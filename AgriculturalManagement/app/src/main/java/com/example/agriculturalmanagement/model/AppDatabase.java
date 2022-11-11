package com.example.agriculturalmanagement.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.agriculturalmanagement.model.dao.CropDao;
import com.example.agriculturalmanagement.model.dao.FieldDao;
import com.example.agriculturalmanagement.model.entities.CalendarEvent;
import com.example.agriculturalmanagement.model.entities.Crop;
import com.example.agriculturalmanagement.model.entities.Field;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        version = 1,
        entities = {
                CalendarEvent.class,
                Crop.class,
                Field.class,
        }
)
@TypeConverters({
        Converters.class,
})
public abstract class AppDatabase extends RoomDatabase {
        private static volatile AppDatabase instance;
        private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(1);

        static AppDatabase getInstance(final Context context) {
                if (instance == null) {
                        synchronized (AppDatabase.class) {
                                if (instance == null) {
                                        instance = Room
                                                .databaseBuilder(
                                                        context.getApplicationContext(),
                                                        AppDatabase.class,
                                                        "app_database")
                                                .addCallback(DB_CALLBACK)
                                                .fallbackToDestructiveMigration() // TODO: revisit
                                                .build();
                                }
                        }
                }

                return instance;
        }

        private static final RoomDatabase.Callback DB_CALLBACK = new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                        EXECUTOR.execute(() -> {

                        });
                }
        };

        public abstract CropDao cropDao();
        public abstract FieldDao fieldDao();
}
