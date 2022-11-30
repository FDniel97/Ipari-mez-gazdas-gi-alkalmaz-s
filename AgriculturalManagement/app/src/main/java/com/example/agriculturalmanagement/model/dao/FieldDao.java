package com.example.agriculturalmanagement.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.agriculturalmanagement.model.entities.Field;
import com.example.agriculturalmanagement.model.entities.FieldWithCropAndEvents;

import java.util.List;

@Dao
public interface FieldDao {
    @Query("SELECT * FROM fields")
    LiveData<List<Field>> getAll();

    @Transaction
    @Query("SELECT * FROM fields")
    LiveData<List<FieldWithCropAndEvents>> getAllWithCropAndEvents();
}
