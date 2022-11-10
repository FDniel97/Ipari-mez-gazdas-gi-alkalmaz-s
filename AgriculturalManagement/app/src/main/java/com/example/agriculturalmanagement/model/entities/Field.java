package com.example.agriculturalmanagement.model.entities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Field{

    private static ArrayList<Integer> fieldIdCache;

    private int id;// consistency if preserved and maintained by database (avoidance of id redundancy)
    private String name;
    private Duration workHours;
    private int precipitationQuantity;
    private int cropType;
    private double overcastIndex;
    private double lightExposure;// intensity related light exposure, computing from historical overcastIndex
    private LocalDateTime created;
    private double locationLongitude;
    private double locationLatitude;
    private PhysicalAddress physicalAddress;

    public Field() throws Exception{

        id = 0;
        name = "";
        workHours = Duration.ZERO;
        precipitationQuantity = 0;
        overcastIndex = 0.0;
        lightExposure = 1.0;
        created = LocalDateTime.now();
        locationLongitude = 0.0;
        locationLatitude = 0.0;
        physicalAddress = new PhysicalAddress();
    }

    /* !@brief Constructor for existing entity creation, getting from database.
    *
    * @param[in] name Name of field
    * @param[in] area Size of area of field
    * @param[in] workHours Hours spent with work on the field
    * @param[in] precipitationQuantity The amount of precipitate received
    * @param[in] cropType Type of crop due to homogeneous cultivation on field
    * @param[in] overcastIndex Overcast index for estimating crop development
    * @param[in] lightExposure Light exposure for estimating crop development
    * @param[in] created Date for creation of field
    * @param[in] locationLongitude Horizontal POI
    * @param[in] locationLatitude Vertical POI
    * @param[in] physicalAddress Postal address including parcel address
    */
    public Field(String name, ComplexArea area, Duration workHours, int precipitationQuantity,
                 int cropType, double overcastIndex, double lightExposure,
                 LocalDateTime created, double locationLongitude, double locationLatitude,
                 PhysicalAddress physicalAddress) throws Exception {

        id = -1;// has not been used, new entity

        setName(name);

        setWorkHours(workHours);

        setPrecipitationQuantity(precipitationQuantity);

        setCropType(cropType);

        setOvercastIndex(overcastIndex);

        setLightExposure(lightExposure);

        if(created != null) this.created = created;
        else throw new Exception("Date of creation is null.");

        setLocation(locationLongitude, locationLatitude);

        setPhysicalAddress(physicalAddress);
    }

    /* !@brief Constructor for existing entity creation, getting from database.
     *
     * @param[in] id Id of record from database
     * @param[in] name Name of the field
     * @param[in] area Size of area of field
     * @param[in] workHours Hours spent with work on the field
     * @param[in] precipitationQuantity The amount of precipitate received
     * @param[in] cropType Type of crop due to homogeneous cultivation on field
     * @param[in] overcastIndex Overcast index for estimating crop development
     * @param[in] lightExposure Light exposure for estimating crop development
     * @param[in] created Date for creation of field
     * @param[in] locationLongitude Horizontal POI
     * @param[in] locationLatitude Vertical POI
     * @param[in] physicalAddress Postal address including parcel address
     */
    public Field(int id, String name, Duration workHours, int precipitationQuantity,
                 int cropType, double overcastIndex, double lightExposure,
                 LocalDateTime created, double locationLongitude, double locationLatitude,
                 PhysicalAddress physicalAddress) {

        // assumption of consistent database (skipping data condition tests)
        this.id = id;
        this.name = name;
        this.workHours = workHours;
        this.precipitationQuantity = precipitationQuantity;
        this.cropType = cropType;
        this.overcastIndex = overcastIndex;
        this.lightExposure = lightExposure;
        this.created = created;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.physicalAddress = physicalAddress;
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

    public Duration getWorkHours() {

        return workHours;
    }

    public int getPrecipitationQuantity() {

        return precipitationQuantity;
    }

    public int getCropType() {

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

    // SETTER METHODS FOR MEMBERS

    public void setName(String new_name) throws Exception{

        if(new_name.isEmpty()) throw new Exception("New name for the field is empty.");
        else this.name = new_name;
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

    /* !@brief Setter that modifies crop, pragmatic inconsistency would occur in this case
    *
    * @param[in] cropType New type crop
    */
    public void setCropType(int cropType) throws Exception{
        this.cropType = cropType;
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
}
