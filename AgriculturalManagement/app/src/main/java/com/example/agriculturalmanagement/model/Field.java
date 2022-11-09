package com.example.agriculturalmanagement.model;

import android.location.Address;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.lang.Exception;
import java.util.Map;

public class Field{

    private static ArrayList<Integer> fieldIdCache;

    private int id;// consistency if preserved and maintained by database (avoidance of id redundancy)
    private String name;
    private Duration workHours;
    private int precipitationQuantity;
    private Map<String, CalendarEvent> events;
    private Crop cropType;
    private double overcastIndex;
    private double lightExposure;// intensity related light exposure, computing from historical overcastIndex
    private LocalDateTime created;
    private double locationLongitude;
    private double locationLatitude;
    private PhysicalAddress physicalAddress;
    private ComplexArea physicalForm;

    public Field() throws Exception{

        id = 0;
        name = "";
        workHours = Duration.ZERO;
        precipitationQuantity = 0;
        events = new HashMap<String, CalendarEvent>();
        cropType = cropType.ZERO;
        overcastIndex = 0.0;
        lightExposure = 1.0;
        created = LocalDateTime.now();
        locationLongitude = 0.0;
        locationLatitude = 0.0;
        physicalAddress = new PhysicalAddress();
        physicalForm = new ComplexArea();
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
    public Field(String name, ComplexArea area, Duration workHours, int precipitationQuantity,
                 Map<String, CalendarEvent> events, Crop cropType, double overcastIndex, double lightExposure,
                 LocalDateTime created, double locationLongitude, double locationLatitude,
                 PhysicalAddress physicalAddress, ComplexArea physicalForm) throws Exception {

        id = -1;// has not been used, new entity

        setName(name);

        setArea(area);

        setWorkHours(workHours);

        setPrecipitationQuantity(precipitationQuantity);

        setEvents(events);

        setCropType(cropType);

        setOvercastIndex(overcastIndex);

        setLightExposure(lightExposure);

        if(created != null) this.created = created;
        else throw new Exception("Date of creation is null.");

        setLocation(locationLongitude, locationLatitude);

        setPhysicalAddress(physicalAddress);

        setPhysicalForm(physicalForm);
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
    public Field(int id, String name, Duration workHours, int precipitationQuantity,
                 Map<String, CalendarEvent> events, Crop cropType, double overcastIndex, double lightExposure,
                 LocalDateTime created, double locationLongitude, double locationLatitude,
                 PhysicalAddress physicalAddress, ComplexArea physicalForm) throws Exception {

        // assumption of consistent database (skipping data condition tests)
        this.id = id;
        this.name = name;
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

        return physicalForm.size();
    }

    public Duration getWorkHours() {

        return workHours;
    }

    public int getPrecipitationQuantity() {

        return precipitationQuantity;
    }

    public CalendarEvent getEvent(String eventName) throws Exception{

        if(events.containsKey(eventName)) return events.get(eventName);

        throw new Exception("Event not found with provided event name.");
    }

    public Map<String, CalendarEvent> getEvents() {

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

    public PhysicalAddress getPhysicalAddress() {

        return physicalAddress;
    }

    public ComplexArea getPhysicalForm() {

        return physicalForm;
    }

    // SETTER METHODS FOR MEMBERS

    public void setName(String new_name) throws Exception{

        if(new_name.isEmpty()) throw new Exception("New name for the field is empty.");
        else this.name = new_name;
    }

    public void setArea(ComplexArea new_area) throws Exception{

        // concatenating new area chunk with existing ones
        //  including overlapping and disjunction error handling

        // TODO...

        if(new_area.size() > 0.0) this.physicalForm = new_area;
        else throw new Exception("Field area is zero.");
    }

    /* !@brief Setter that updates work hours strictly in increasing tendency, decrease can not be occurred
    *
    * @param[in] workHours Number of work hours to add to the recent hours
    */
    public void setWorkHours(Duration workHours) throws Exception{


        if(workHours == null) throw new Exception("Work hours is null.");

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
    * @param[in] newEvent Event to be added into the event list
    */
    public void setEvent(CalendarEvent newEvent) throws Exception{

        if(newEvent != null){

            // no further handling is needed, internal conditions were handled in type
            if(LocalDateTime.now().isBefore(newEvent.getTimestamp())) this.events.put(newEvent.getName(), newEvent);
            else throw new Exception("CalendarEvent was before current time.");
        }
        else{

            throw new Exception("CalendarEvent is null.");
        }
    }

    /* !@brief Setter that inserts multiple new events into event list. Only future events can be placed
    *          into the list.
    *
    * @param[i] newEvents Events to be added to the event list
    * */
    public void setEvents(Map<String, CalendarEvent> newEvents) throws Exception{

        if(events.isEmpty()) throw new Exception("Event list is empty.");

        int sizeOfNewEvents = newEvents.size();

        for(int i = 0; i < sizeOfNewEvents; ++i){

            setEvent(newEvents.get(i));
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

    public void setPhysicalAddress(PhysicalAddress physicalAddress) throws Exception{

        if(physicalAddress != null) this.physicalAddress = physicalAddress;
        else throw new Exception("Physical address is null.");
    }

    public void setPhysicalForm(ComplexArea physicalForm) throws Exception{

        if(physicalForm != null){

            // validation of physicalFrom is before passing to Field constructor
            if(physicalForm.size() > 0.0) this.physicalForm = physicalForm;
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

    public static Field search(Field fieldParams) throws Exception{

        Field fieldResult = new Field();

        // TODO...
        // iteration on database elements AND using precache buffer...

        return fieldResult;
    }
}
