package com.example.agriculturalmanagement.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

@Entity(
        tableName = "calendar_events",
        foreignKeys = {
                @ForeignKey(
                        entity = Field.class,
                        parentColumns = "id",
                        childColumns = "fieldId",
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class CalendarEvent {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int fieldId;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private Date timestamp;

    /*! @brief Constructor which initializes the object in case of existing record from database
    *
    * @param[in] id Id of entity in database
    * @param[in] fieldId Index that refers to the specific selected field
    * @param[in] name The name of the event
    * @param[in] timestamp Time when the event has been scheduled for
    * @param[in] description Description text about event
    */
    public CalendarEvent(int id, int fieldId, String name, Date timestamp, String description){

        // assume that the database entries are consistent

        this.id = id;
        this.fieldId = fieldId;
        this.name = name;
        this.timestamp = timestamp;
        this.description = description;
    }

    /* !@brief Constructor which creates a new entity and this will be uploaded into database later
    *
    * @param[in] fieldId Identifier of given attached field
    * @param[in] name Name of event
    * @param[in] timestamp Time when the event has been scheduled for
    * @param[in] description Description text about event
    */
    @Ignore
    public CalendarEvent(int fieldId, String name, Date timestamp, String description) throws Exception{
        boolean found = false;
        ArrayList<Integer> fieldIdCache = Field.getFieldIdCache();
        int sizeOfFieldIdCache = fieldIdCache.size();

        for(int i = 0; i < sizeOfFieldIdCache && !found; ++i)
            found = found && fieldId == fieldIdCache.get(i).intValue();

        if(found) this.fieldId = fieldId;
        else throw new Exception("Field can not be found with this id.");

        if(name.isEmpty()) throw new Exception("Field name is empty.");
        else this.name = name;

        if(timestamp != null) this.timestamp = timestamp;
        else throw new Exception("Timestamp is null.");

        // this might be empty?
        if(description.isEmpty()) throw new Exception("Description is empty.");
        else this.description = description;
    }

    // GETTER METHODS FOR MEMBERS

    public int getId(){

        return id;
    }

    public int getFieldId(){

        return fieldId;
    }

    public String getName(){

        return name;
    }

    public Date getTimestamp(){

        return timestamp;
    }

    public String getDescription(){

        return description;
    }

    // SETTER METHODS FOR MEMBERS

    public void setFieldId(int fieldId) throws Exception{

        boolean found = false;
        ArrayList<Integer> fieldIdCache = Field.getFieldIdCache();
        int sizeOfFieldIdCache = fieldIdCache.size();

        for(int i = 0; i < sizeOfFieldIdCache && !found; ++i)
            found = found && fieldId == fieldIdCache.get(i).intValue();

        if(found) this.fieldId = fieldId;
        else throw new Exception("Field can not be found with this id.");
    }

    public void setName(String name) throws Exception{

        if(name.isEmpty()) throw new Exception("Field name is empty.");
        else this.name = name;
    }

    public void setTimestamp(Date timestamp) throws Exception{

        if(timestamp != null) this.timestamp = timestamp;
        else throw new Exception("Timestamp is null.");
    }

    public void setDescription(String description) throws Exception{

        // this might be empty?
        if(description.isEmpty()) throw new Exception("Description is empty.");
        else this.description = description;
    }
}
