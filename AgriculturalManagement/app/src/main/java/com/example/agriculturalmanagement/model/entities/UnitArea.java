package com.example.agriculturalmanagement.model.entities;

import java.lang.String;

public class UnitArea implements GenArea{

    private static double longitudeLength;
    private static double latitudeLength;

    private double locationLongitude;
    private double locationLatitude;

    public UnitArea(String rawData){

        // TODO...
    }

    public double getLongitude(){

        return locationLongitude;
    }

    public double getLatitude(){

        return locationLatitude;
    }
    @Override
    public double size() {

        // TODO...

        return 0;
    }

    @Override
    public void computeUnitArea() {

        // TODO...
    }

    @Override
    public String toString(){

        // TODO...

        return "";
    }
}
