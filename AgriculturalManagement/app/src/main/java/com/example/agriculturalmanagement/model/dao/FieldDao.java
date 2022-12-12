package com.example.agriculturalmanagement.model.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

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

    @Transaction
    @Insert
    void insert(Field field);

    @Query("DELETE FROM fields")
    void deleteAll();

    @Query("DELETE FROM fields WHERE id = :fieldId")
    void deleteById(int fieldId);

    @Query("SELECT * FROM fields WHERE id = :fieldId")
    LiveData<Field> getById(int fieldId);

    @Query("SELECT COUNT(*) FROM fields WHERE cropType = :cropId")
    LiveData<Integer> getCountForCrop(int cropId);
}
