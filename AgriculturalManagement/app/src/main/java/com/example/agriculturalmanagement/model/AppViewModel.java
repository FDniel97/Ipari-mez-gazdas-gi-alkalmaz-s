package com.example.agriculturalmanagement.model;

import android.app.Application;
import android.os.Handler;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.agriculturalmanagement.model.dao.FieldDao;
import com.example.agriculturalmanagement.model.entities.Field;
import com.example.agriculturalmanagement.util.ResultReceiver;

import java.util.List;
import java.util.function.Supplier;

public class AppViewModel extends AndroidViewModel {
    private final AppDatabase db;
    private final Handler handler;

    private final FieldDao fieldDao;
    private final LiveData<List<Field>> fields;

    public AppViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        handler = new Handler(application.getMainLooper());

        fieldDao = db.fieldDao();
        fields = fieldDao.getAll();
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
