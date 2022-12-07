package com.example.agriculturalmanagement.util;

import androidx.annotation.NonNull;

public interface ResultReceiver<T> {
    default void onSuccess(T value) { }
    default void onFailure(@NonNull Exception e) { }
}
