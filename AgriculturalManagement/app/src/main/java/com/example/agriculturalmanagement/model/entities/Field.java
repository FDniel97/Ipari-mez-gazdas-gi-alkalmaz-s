package com.example.agriculturalmanagement.model.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Entity(
        tableName = "fields",
        foreignKeys = {
                @ForeignKey(
                        entity = Crop.class,
                        parentColumns = "id",
                        childColumns = "cropType",
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.RESTRICT
                )
        }
)
public class Field {

    private static ArrayList<Integer> fieldIdCache;

    @PrimaryKey(autoGenerate = true)
    private int id;// consistency if preserved and maintained by database (avoidance of id redundancy)

    @NonNull
    private String name;

    @NonNull
    private Duration workHours;

    private int precipitationQuantity;
    private int cropType;
    private double radiation;// intensity related light exposure, computing from historical overcastIndex

    @NonNull
    private Date creationDate;
    private double locationLongitude;
    private double locationLatitude;

    @NonNull
    @Embedded
    private PhysicalAddress physicalAddress;

    @NonNull
    @Embedded
    private ComplexArea physicalGeometry;

    public void fromString(String rawData) throws Exception {

        String[] rawDataArr = rawData.split(":");
        if(rawDataArr.length == 11) {

            id = Integer.parseInt(rawDataArr[0]);
            name = rawDataArr[1];
            workHours = Duration.ofHours(Integer.parseInt(rawDataArr[2]));
            precipitationQuantity = Integer.parseInt(rawDataArr[3]);
            cropType = Integer.parseInt(rawDataArr[4]);
            radiation = Double.parseDouble(rawDataArr[5]);
            creationDate = new SimpleDateFormat().parse(rawDataArr[6]);
            locationLongitude = Double.parseDouble(rawDataArr[7]);
            locationLatitude = Double.parseDouble(rawDataArr[8]);
            physicalAddress.fromString(rawDataArr[9]);
            physicalGeometry.fromString(rawDataArr[10]);
        }
    }

    public String toString() {

        return id + ":" +
               name + ":" +
               workHours + ":" +
               precipitationQuantity + ":" +
               cropType + ":" +
               radiation + ":" +
               creationDate.toString() + ":" +
               locationLongitude + ":" +
               locationLatitude + ":" +
               physicalAddress.toString() + ":" +
               physicalGeometry.toString();
    }

    public Field(){

        name = "";
        workHours = Duration.ZERO;
        precipitationQuantity = 0;
        cropType = 0;
        radiation  = 0.0;
        creationDate = Calendar.getInstance().getTime();
        locationLatitude = 0.0;
        locationLongitude = 0.0;
        physicalAddress = new PhysicalAddress();
    }


    /* !@brief Constructor for existing entity creation, getting from database.
    *
    * @param[in] name Name of field
    * @param[in] area Size of area of field
    * @param[in] workHours Hours spent with work on the field
    * @param[in] precipitationQuantity The amount of precipitate received
    * @param[in] cropType Type of crop due to homogeneous cultivation on field
    * @param[in] radiaton radiation intensity of light for estimating crop development
    * @param[in] locationLongitude Horizontal POI
    * @param[in] locationLatitude Vertical POI
    * @param[in] physicalAddress Postal address including parcel address
    */
    public Field(String name, Duration workHours, int precipitationQuantity,
                 int cropType, double radiation, double locationLongitude, double locationLatitude,
                 PhysicalAddress physicalAddress, ComplexArea physicalGeometry) throws Exception {

        id = -1;// has not been used, new entity

        setName(name);

        setWorkHours(workHours);

        setPrecipitationQuantity(precipitationQuantity);

        setCropType(cropType);

        setRadiation(radiation);

        setLocationLongitude(locationLongitude);

        setLocationLatitude(locationLatitude);

        setPhysicalAddress(physicalAddress);

        setPhysicalGeometry(physicalGeometry);
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
     * @param[in] locationLongitude Horizontal POI
     * @param[in] locationLatitude Vertical POI
     * @param[in] physicalAddress Postal address including parcel address
     */
    public Field(int id, String name, Duration workHours, int precipitationQuantity,
                 int cropType, double radiation, double locationLongitude, double locationLatitude,
                 PhysicalAddress physicalAddress, ComplexArea physicalGeometry) {

        // assumption of consistent database (skipping data condition tests)
        this.id = id;
        this.name = name;
        this.workHours = workHours;
        this.precipitationQuantity = precipitationQuantity;
        this.cropType = cropType;
        this.radiation = radiation;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.physicalAddress = physicalAddress;
        this.physicalGeometry = physicalGeometry;
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

    public double getRadiation() {

        return radiation;
    }

    public Date getCreationDate() {

        return creationDate;
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

    public ComplexArea getPhysicalGeometry() {

        return physicalGeometry;
    }

    // SETTER METHODS FOR MEMBERS

    public void setId(int id){

        this.id = id;
    }

    public void setName(String newName) throws Exception{

        if(newName.isEmpty()) throw new Exception("New name for the field is empty.");
        else this.name = newName;
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

    public void setRadiation(double radiation) throws Exception{

        if(radiation < 0) this.radiation = radiation;
        else throw new Exception("Radiation is out of bounds.");
    }

    public void setCreationDate(Date creationDate){

        this.creationDate = creationDate;
    }

    public void setLocationLongitude(double locationLongitude) throws Exception{

        if(locationLongitude < 180.0 && locationLongitude > -180.0) this.locationLongitude = locationLongitude;
        else throw new Exception("Location longitude is out of bounds.");
    }

    public void setLocationLatitude(double locationLatitude) throws Exception{

        if(locationLatitude < 90.0 && locationLatitude > -90.0) this.locationLatitude = locationLatitude;
        else throw new Exception("Location latitude is out of bounds.");
    }

    public void setPhysicalAddress(PhysicalAddress physicalAddress) throws Exception{

        if(physicalAddress != null) this.physicalAddress = physicalAddress;
        else throw new Exception("Physical address is null.");
    }

    public void setPhysicalGeometry(ComplexArea physicalGeometry) throws Exception {

        if(physicalGeometry != null) this.physicalGeometry = physicalGeometry;
        else throw new Exception("Physical geometry is null.");
    }
}
