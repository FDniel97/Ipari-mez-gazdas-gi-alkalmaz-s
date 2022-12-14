package com.example.agriculturalmanagement.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agriculturalmanagement.model.entities.CalendarEvent;

import java.util.List;

@Dao
public interface CalendarEventDao {
    @Query("SELECT * FROM calendar_events ORDER BY timestamp ASC")
    LiveData<List<CalendarEvent>> getAll();

    @Query("SELECT * FROM calendar_events WHERE id = :eventId")
    LiveData<CalendarEvent> getById(int eventId);

    @Query("SELECT * FROM calendar_events WHERE :from <= timestamp AND timestamp < :to ORDER BY timestamp ASC")
    LiveData<List<CalendarEvent>> getInRange(long from, long to);

    @Insert
    void insert(CalendarEvent calendarEvent);

    @Update
    void update(CalendarEvent calendarEvent);

    @Query("DELETE FROM calendar_events")
    void deleteAll();

    @Query("DELETE FROM calendar_events WHERE id = :eventId")
    void deleteById(int eventId);
}
