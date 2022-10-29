package com.example.agriculturalmanagement.model;

import android.location.Address;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.lang.Exception;

public class Field{

    private static ArrayList<Integer> fieldIdCache;

    private int id;// consistency if preserved and maintained by database (avoidance of id redundancy)
    private String name;
    private double area;
    private Duration workHours;
    private int precipitationQuantity;
    private List<CalendarEvent> events;
    private Crop cropType;
    private double overcastIndex;
    private double lightExposure;// intensity related light exposure, computing from historical overcastIndex
    private LocalDateTime created;
    private double locationLongitude;
    private double locationLatitude;
    private Address physicalAddress;
    private GenArea physicalForm;

    public Field(){

        // TODO...
    }

    /* !@brief Constructor for existing entity creation, getting from database.
    *
    * @param[in] name Name of field
    * @param[in] area Size of area of field
    * @param[in] workHours Hours spent with work on the field
    * @param[in] precipitationQuantity The amount of precipitate received
    * @param[in] events Assigned event to the field
    * @param[in] cropType Type of crop due to homogeneous cultivation on field
    * @param[in] overcastIndex Overcast index for estimating crop development
    * @param[in] lightExposure Light exposure for estimating crop development
    * @param[in] created Date for creation of field
    * @param[in] locationLongitude Horizontal POI
    * @param[in] locationLatitude Vertical POI
    * @param[in] physicalAddress Postal address including parcel address
    * @param[in] physicalForm Estimated geometric representation of field
    */
    public Field(String name, double area, Duration workHours, int precipitationQuantity,
                 List<CalendarEvent> events, Crop cropType, double overcastIndex, double lightExposure,
                 LocalDateTime created, double locationLongitude, double locationLatitude,
                 Address physicalAddress, GenArea physicalForm) throws Exception {

        id = -1;// has not been used, new entity

        if(area > 0.0) this.area = area;
        else throw new Exception("Field area is zero.");

        if(workHours != null) this.workHours = workHours;
        else throw new Exception("Work hours is null.");

        if(precipitationQuantity >= 0) this.precipitationQuantity = precipitationQuantity;
        else throw new Exception("Precipitation quantity if zero.");

        if(events.size() > 0) this.events = events;
        else throw new Exception("Event list is empty.");

        if(cropType != null) this.cropType = cropType;
        else throw new Exception("Crop type is null.");

        if(overcastIndex < 1 && overcastIndex > 0) this.overcastIndex = overcastIndex;
        else throw new Exception("Overcast index is out of bounds.");

        if(lightExposure < 1 && lightExposure > 0) this.lightExposure = lightExposure;
        else throw new Exception("Light exposure index is out of bounds.");

        if(created != null) this.created = created;
        else throw new Exception("Date of creation is null.");

        setLocation(locationLongitude, locationLatitude);

        setPhysicalAddress(physicalAddress);

        if(physicalForm != null){

            // validation of physicalFrom is before passing to Field constructor
            if(physicalForm.Size() > 0.0) this.physicalForm = physicalForm;
            else throw new Exception("Area of field is zero.");
        }
        else{

            throw new Exception("Area of field is null.");
        }
    }

    /* !@brief Constructor for existing entity creation, getting from database.
     *
     * @param[in] id Id of record from database
     * @param[in] name Name of the field
     * @param[in] area Size of area of field
     * @param[in] workHours Hours spent with work on the field
     * @param[in] precipitationQuantity The amount of precipitate received
     * @param[in] events Assigned event to the field
     * @param[in] cropType Type of crop due to homogeneous cultivation on field
     * @param[in] overcastIndex Overcast index for estimating crop development
     * @param[in] lightExposure Light exposure for estimating crop development
     * @param[in] created Date for creation of field
     * @param[in] locationLongitude Horizontal POI
     * @param[in] locationLatitude Vertical POI
     * @param[in] physicalAddress Postal address including parcel address
     * @param[in] physicalForm estimated geometric representation of field
     */
    public Field(int id, String name, double area, Duration workHours, int precipitationQuantity,
                 List<CalendarEvent> events, Crop cropType, double overcastIndex, double lightExposure,
                 LocalDateTime created, double locationLongitude, double locationLatitude,
                 Address physicalAddress, GenArea physicalForm) throws Exception {

        // assumption of consistent database (skipping data condition tests)
        this.id = id;
        this.name = name;
        this.area = area;
        this.workHours = workHours;
        this.precipitationQuantity = precipitationQuantity;
        this.events = events;
        this.cropType = cropType;
        this.overcastIndex = overcastIndex;
        this.lightExposure = lightExposure;
        this.created = created;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.physicalAddress = physicalAddress;
        this.physicalForm = physicalForm;
    }

    // GETTERS METHODS FOR MEMBERS

    public static ArrayList<Integer> getFieldIdCache(){

        return fieldIdCache;
    }

    public int getId() {

        return id;
    }

    public String getName(){

        return name;
    }

    public double getArea() {

        return area;
    }

    public Duration getWorkHours() {

        return workHours;
    }

    public int getPrecipitationQuantity() {

        return precipitationQuantity;
    }

    public List<CalendarEvent> getEvents() {

        return events;
    }

    public Crop getCropType() {

        return cropType;
    }

    public double getOvercastIndex() {

        return overcastIndex;
    }

    public double getLightExposure() {

        return lightExposure;
    }

    public LocalDateTime getCreated() {

        return created;
    }

    public double getLocationLongitude() {

        return locationLongitude;
    }

    public double getLocationLatitude() {

        return locationLatitude;
    }

    public Address getPhysicalAddress() {

        return physicalAddress;
    }

    public GenArea getPhysicalForm() {

        return physicalForm;
    }

    // SETTER METHODS FOR MEMBERS

    public void setName(String new_name) throws Exception{

        if(new_name.isEmpty()) throw new Exception("New name for the field is empty.");
        else this.name = new_name;
    }

    public void setArea(GenArea new_area_chunk) {

        // concatenating new area chunk with existing ones
        //  including overlapping and disjunction error handling

        // TODO...
    }

    /* !@brief Setter that updates work hours strictly in increasing tendency, decrease can not be occurred
    *
    * @param[in] workHours Number of work hours to add to the recent hours
    */
    public void setWorkHours(Duration workHours) throws Exception{

        if(workHours.compareTo(Duration.ZERO) > 0) this.workHours.plus(workHours);
        else throw new Exception("Number of additional hours are zero.");
    }

    /*! @brief Setter that updates received precipitate using the recent amount
    *
    * @param[in] precipitationQuantity Amount of precipitate to be added
    */
    public void setPrecipitationQuantity(int precipitationQuantity) throws Exception{

        if(precipitationQuantity >= 0) this.precipitationQuantity = precipitationQuantity;
        else throw new Exception("Negative precipitation quantity has provided.");
    }

    /*! @brief Setter that inserts a new event into the event list. Only future events can be placed
    *          into the list.
    * @param[in] new_event Event to be added into the event list
    */
    public void setEvent(CalendarEvent new_event) throws Exception{

        if(new_event != null){

            // no further handling is needed, internal conditions were handled in type
            if(LocalDateTime.now().isBefore(new_event.getTimestamp())) this.events.add(new_event);
            else throw new Exception("CalendarEvent was before current time.");
        }
        else{

            throw new Exception("CalendarEvent is null.");
        }
    }

    /* !@brief Setter that modifies crop, pragmatic inconsistency would occur in this case
    *
    * @param[in] cropType New type crop
    */
    public void setCropType(Crop cropType) throws Exception{

        if(cropType != null) this.cropType = cropType;
        else throw new Exception("Crop type is null.");
    }

    public void setOvercastIndex(double overcastIndex) throws Exception{

        if(overcastIndex < 1 && overcastIndex > 0) this.overcastIndex = overcastIndex;
        else throw new Exception("Overcast index is out of bounds.");
    }

    public void setLightExposure(double lightExposure) throws Exception{

        if(lightExposure < 1 && lightExposure > 0) this.lightExposure = lightExposure;
        else throw new Exception("Light exposure index is out of bounds.");
    }

    public void setLocation(double locationLongitude, double locationLatitude) throws Exception{

        if(locationLongitude < 180.0 && locationLongitude > -180.0) this.locationLongitude = locationLongitude;
        else throw new Exception("Location longitude is out of bounds.");

        if(locationLatitude < 90.0 && locationLatitude > -90.0) this.locationLatitude = locationLatitude;
        else throw new Exception("Location latitude is out of bounds.");
    }

    public void setPhysicalAddress(Address physicalAddress) throws Exception{

        if(physicalAddress != null) this.physicalAddress = physicalAddress;
        else throw new Exception("Physical address is null.");
    }

    public void setPhysicalForm(GenArea physicalForm) throws Exception{

        if(physicalForm != null){

            if(physicalForm.Size() > 0.0) this.physicalForm = physicalForm;
            else throw new Exception("Area of field is zero.");
        }
        else{

            throw new Exception("Area of field is null.");
        }
    }

    // DATABASE MANAGEMENT
    public static boolean updateDatabase(){

        // TODO...

        return false;
    }

    public static Field Search(Field field_params) throws Exception{

        Field field_result = new Field();

        // TODO...
        // iteration on database elements AND using precache buffer...

        return field_result;
    }
}
