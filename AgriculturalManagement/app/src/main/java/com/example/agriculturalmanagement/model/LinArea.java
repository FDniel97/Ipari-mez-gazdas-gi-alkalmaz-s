package com.example.agriculturalmanagement.model;

import java.lang.String;
import java.util.Dictionary;
import java.util.List;

public class LinArea implements GenArea{

    private Dictionary<Double, Double> coords;
    private double locationLongitude;
    private double locationLatitude;


    public LinArea(){

        // TODO...
    }

    public LinArea(String rawData){

        // TODO...
    }

    public double getLongitude(){

        return locationLongitude;
    }

    public double getLocationLatitude(){

        return locationLatitude;
    }

    @Override
    public void computeUnitArea() {

        // TODO...
    }

    public boolean checkLoop(){

        return true;
    }

    public void addCorrd(){

        // TODO...
    }

    @Override
    public double size() {

        // TODO...

        return 0;
    }

    @Override
    public String toString(){

        // TODO...

        return "";
    }
}
