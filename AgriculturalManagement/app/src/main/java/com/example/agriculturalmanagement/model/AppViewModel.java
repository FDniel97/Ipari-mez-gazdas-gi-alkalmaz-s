package com.example.agriculturalmanagement.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.agriculturalmanagement.model.dao.FieldDao;
import com.example.agriculturalmanagement.model.entities.Field;

import java.util.List;

public class AppViewModel extends AndroidViewModel {
    private AppDatabase db;

    private final FieldDao fieldDao;
    private final LiveData<List<Field>> fields;

    public AppViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application);

        fieldDao = db.fieldDao();
        fields = fieldDao.getAll();
    }

    public LiveData<List<Field>> getAllFields() {
        return fields;
    }
}
