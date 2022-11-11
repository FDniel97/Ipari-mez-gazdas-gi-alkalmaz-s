package com.example.agriculturalmanagement.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.agriculturalmanagement.model.entities.FieldWithCropAndEvents;

import java.util.List;

@Dao
public interface FieldDao {
    @Transaction
    @Query("SELECT * FROM fields")
    LiveData<List<FieldWithCropAndEvents>> getAll();
}
