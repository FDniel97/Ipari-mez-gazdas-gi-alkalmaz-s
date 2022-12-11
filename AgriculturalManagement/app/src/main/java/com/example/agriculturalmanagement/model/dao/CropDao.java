package com.example.agriculturalmanagement.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.agriculturalmanagement.model.entities.Crop;

import java.util.List;

@Dao
public interface CropDao {
    @Insert
    void insert(Crop crop);

    @Delete
    void delete(Crop crop);

    @Query("DELETE FROM crops WHERE id = :cropId")
    void deleteById(int cropId);

    @Query("DELETE FROM crops")
    void deleteAll();

    @Query("SELECT * FROM crops")
    LiveData<List<Crop>> getAll();

    @Query("SELECT * FROM crops WHERE id = :cropId")
    LiveData<Crop> getById(int cropId);
}
