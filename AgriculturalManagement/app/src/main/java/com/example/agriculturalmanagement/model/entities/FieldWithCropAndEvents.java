package com.example.agriculturalmanagement.model.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class FieldWithCropAndEvents {
    @Embedded
    public Field field;

    @Relation(
            parentColumn = "cropType",
            entityColumn = "id"
    )
    public Crop crop;

    @Relation(
            parentColumn = "id",
            entityColumn = "fieldId"
    )
    public List<CalendarEvent> events;
}
