package com.example.agriculturalmanagement.model;

import android.app.Application;
import android.os.Handler;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.agriculturalmanagement.model.dao.CropDao;
import com.example.agriculturalmanagement.model.dao.FieldDao;
import com.example.agriculturalmanagement.model.entities.Crop;
import com.example.agriculturalmanagement.model.entities.Field;
import com.example.agriculturalmanagement.util.ResultReceiver;

import java.util.List;
import java.util.function.Supplier;

public class AppViewModel extends AndroidViewModel {
    private final AppDatabase db;
    private final Handler handler;

    private final FieldDao fieldDao;
    private final LiveData<List<Field>> fields;

    private final CropDao cropDao;
    private final LiveData<List<Crop>> crops;

    public AppViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        handler = new Handler(application.getMainLooper());

        fieldDao = db.fieldDao();
        fields = fieldDao.getAll();

        cropDao = db.cropDao();
        crops = cropDao.getAll();
    }

    public LiveData<List<Field>> getAllFields() {
        return fields;
    }

    public LiveData<Field> getFieldById(int fieldId) {
        return fieldDao.getById(fieldId);
    }

    public void deleteFieldById(int fieldId, ResultReceiver<Void> resultReceiver) {
        doDbOperation(() -> {
            fieldDao.deleteById(fieldId);
            return null;
        }, resultReceiver);
    }

    public LiveData<List<Crop>> getAllCrops() {
        return crops;
    }

    public LiveData<Crop> getCropById(int cropId) {
        return cropDao.getById(cropId);
    }

    public void deleteCropById(int cropId, ResultReceiver<Void> resultReceiver) {
        doDbOperation(() -> {
            cropDao.deleteById(cropId);
            return null;
        }, resultReceiver);
    }

    public void insertCrop(Crop crop, ResultReceiver<Void> resultReceiver) {
        doDbOperation(() -> {
            cropDao.insert(crop);
            return null;
        }, resultReceiver);
    }

    public void updateCrop(Crop crop, ResultReceiver<Void> resultReceiver) {
        doDbOperation(() -> {
            cropDao.update(crop);
            return null;
        }, resultReceiver);
    }

    // the ResultReceiver callbacks are run on the main thread of the application
    private <T> void doDbOperation(Supplier<T> operation, ResultReceiver<T> resultReceiver) {
        db.execute(() -> {
            try {
                var result = operation.get();
                handler.post(() -> resultReceiver.onSuccess(result));
            } catch (Exception e) {
                handler.post(() -> resultReceiver.onFailure(e));
            }
        });
    }
}
