package com.example.agriculturalmanagement.model.entities;

import java.lang.String;
import java.lang.Exception;

// GEO COORDINATE CLASS
public class POI{

    public double ang;// angular region identifier
    public double lon;
    public double lat;
    public int overlapDir;// vertex count of clockwise backward overlap in order to determine positive areas


    public POI(String rawData) throws Exception{

        // assumption: all attributes are valid due to past evaluation at first instantiation
        //  of initial persistence
        String[] rawDataArr = rawData.split("@");

        if(rawDataArr.length != 4) throw new Exception("No appropriate data has provided.");

        try{

            ang = Double.parseDouble(rawDataArr[0]);
            lon = Double.parseDouble(rawDataArr[1]);
            lat = Double.parseDouble(rawDataArr[2]);
            overlapDir = Integer.parseInt(rawDataArr[3]);
        }
        catch(Exception e){

            throw new Exception("An error has occurred during parsing numeral values.");
        }
    }


    public POI(double ang, double lon, double lat, int overlapDir) throws Exception{

        if(ang < 0.0 || 360.0 <= ang) throw new Exception("Orientation identifier is out of "
                + "range (0.0, 360.0].");

        this.ang = ang;

        if(lon < -180.0 || 180.0 <= lon) throw new Exception("Longitudinal location data is out "
                + "of range (-180.0, 180.0]).");

        this.lon = lon;

        if(lat <= -90.0 || 90.0 <= lat) throw new Exception("Latitudinal location data is out "
                + "of range [-90.0, 90.0].");

        this.lat = lat;

        // assume that arbitrary amount of overlaps are allowed
        if(overlapDir < 0) throw new Exception("Negative value of overlap direction.");

        this.overlapDir = overlapDir;
    }


    @Override
    public String toString(){

        return ang + "@" + lon + "@" + lat + "@" + overlapDir;
    }
}
