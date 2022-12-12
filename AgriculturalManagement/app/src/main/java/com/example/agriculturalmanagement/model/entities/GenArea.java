package com.example.agriculturalmanagement.model.entities;

import com.example.agriculturalmanagement.model.LinTreeMap;
import com.example.agriculturalmanagement.model.GenAreaKey;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.lang.Exception;
import java.lang.Math;

public class GenArea {

    protected double locationLongitude;// aggregated and weighthened central position
    protected double locationLatitude;// aggregated and weighthened central position

    protected static double minimalArea = 0.00000000032278814;// above 4 square meters

    // equatorial circumference of Earth
    protected final static double circEq =  40075017;

    // polar circumference of Earth in meters
    protected final static double circPol = 40007863;

    protected double areaSize;// size of computed area

    // data manipulation is computation expensive but the access is much lower
    LinTreeMap<GenAreaKey, POI> coords;
    ArrayList<Integer> coordOrder;// order according to geometry

    public GenArea(){

        locationLongitude = 0.0;
        locationLatitude = 0.0;
        areaSize = 0.0;
        coords = new LinTreeMap<GenAreaKey, POI>();
        coordOrder = new ArrayList<Integer>();
    }

    public double size() {

        return areaSize;
    }

    public int POISize(){

        return coords.size();
    }


    public double getLongitude(){

        return locationLongitude;
    }


    public double getLatitude(){

        return locationLatitude;
    }

    public void addCoord(double lonVal, double latVal) throws Exception{


    }

    public POI getByInd(int i) throws Exception{

        return coords.getByInd(i);
    }

    public POI getByAng(GenAreaKey key){

        return coords.getByKey(key);
    }

    public GenAreaKey getKeyByInd(int i) throws Exception{

        return coords.getKeyByInd(i);
    }

    public POI getByOrd(int i) throws Exception{

        return coords.getByInd(coordOrder.get(i));
    }


    /*! @brief Sets a new longitudinal coordinate value to unit area. The fixedStartLocation indicates
     *    whether a fixed size scaling or a moving unit area is the case.
     * */
    public void setLon(double newLon, boolean fixedStartLocation) throws Exception{

        if(locationLongitude < -180.0 || 180.0 <= locationLongitude)
            throw new Exception("Longitudinal location data is out of range (-180.0, 180.0]).");

        this.locationLongitude = locationLongitude;
    }

    /*! @brief Sets a new latitudinal coordinate vlaue ot unit area. The fixedStartLocation indicates
     *    whether a fixed size scaling or a moving unit area is the case.
     * */
    public void setLat(double newLat, boolean fixedStartLocation) throws Exception{

        if(locationLatitude <= -90.0 || 90.0 <= locationLatitude)
            throw new Exception("Latitudinal location data is out of range [-90.0, 90.0].");

        this.locationLatitude = locationLatitude;
    }


    /* !@brief Sets new minimal value of unit size of area
     *          using a coordinate conversion by the equatorial Earth circumference
     *
     * @param[in] newMinimalArea The new size in square meters*/
    public static void setMinimalArea(double newMinimalArea) throws Exception{

        newMinimalArea /= circEq * circEq;
        newMinimalArea *= 360.0 * 360.0;

        if(newMinimalArea < 0.00000000032278813)// equivalent to 4 square meters close to equator
            throw new Exception("Minimal area size is less than allowed.");

        minimalArea = newMinimalArea;
    }


    public void computeUnitAreaSize() throws Exception{


    }

    public String toString() {

        return "";
    }

    public void fromString(String rawData) {

    }
}
