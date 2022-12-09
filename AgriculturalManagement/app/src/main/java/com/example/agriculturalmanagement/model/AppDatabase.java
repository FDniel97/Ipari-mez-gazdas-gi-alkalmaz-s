package com.example.agriculturalmanagement.model;

import android.content.Context;
import android.icu.util.Calendar;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.agriculturalmanagement.model.dao.CalendarEventDao;
import com.example.agriculturalmanagement.model.dao.CropDao;
import com.example.agriculturalmanagement.model.dao.FieldDao;
import com.example.agriculturalmanagement.model.entities.CalendarEvent;
import com.example.agriculturalmanagement.model.entities.Crop;
import com.example.agriculturalmanagement.model.entities.Field;
import com.example.agriculturalmanagement.model.entities.PhysicalAddress;

import java.time.Duration;
import java.util.Date;
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
                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);

                        EXECUTOR.execute(() -> {

                                var fieldDao = instance.fieldDao();
                                var cropDao = instance.cropDao();

                                fieldDao.deleteAll();
                                cropDao.deleteAll();

                                cropDao.insert(new Crop(1, "corn", "corn corn", 100, 13));

                                try {
                                        for (int i = 1; i <= 10; i++)
                                                fieldDao.insert(new Field(
                                                        i,
                                                        "field " + i,
                                                        Duration.ZERO,
                                                        -1,
                                                        1,
                                                        2.3,
                                                        4.5,
                                                        19.0 + i,
                                                        47.5 - i,
                                                        new PhysicalAddress(
                                                                1111,
                                                                "HU",
                                                                "x",
                                                                "x",
                                                                "x",
                                                                "x",
                                                                "x",
                                                                "x"
                                                        )
                                                ));
                                } catch (Exception e) {
                                        e.printStackTrace();
                                }

                                var now = System.currentTimeMillis();
                                var calendarEventDao = instance.calendarEventDao();
                                calendarEventDao.deleteAll();

                                for (int i = 1; i <= 24; i++) {
                                        calendarEventDao.insert(new CalendarEvent(
                                                0,
                                                i % 10 + 1,
                                                "event #" + i,
                                                new Date(now),
                                                "the event #" + i + " description"
                                        ));

                                        now += 3600 * 1000;
                                }
                        });
                }

                @Override
                public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                        super.onDestructiveMigration(db);
                }
        };

        public void execute(Runnable runnable) {
                EXECUTOR.execute(runnable);
        }

        public abstract CropDao cropDao();
        public abstract FieldDao fieldDao();
        public abstract CalendarEventDao calendarEventDao();
}
