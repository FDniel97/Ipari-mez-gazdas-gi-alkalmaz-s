package com.example.agriculturalmanagement.model.entities;

import java.lang.String;
import java.util.List;

public class UnitArea extends GenArea{

    public static List<UnitArea> unitAreas;

    private static double minAreaLength = 0.0000179664;// above 2m

    private double areaWidth;
    private double areaHeight;


    public UnitArea(double areaWidth, double areaHeight, double lon, double lat) throws Exception{

        super();

        coords.add(new LinTreeMap.Pair<GenAreaKey, POI>(new GenAreaKey(315.0), new POI()));
        coords.add(new LinTreeMap.Pair<GenAreaKey, POI>(new GenAreaKey(45.0), new POI()));
        coords.add(new LinTreeMap.Pair<GenAreaKey, POI>(new GenAreaKey(135.0), new POI()));
        coords.add(new LinTreeMap.Pair<GenAreaKey, POI>(new GenAreaKey(225.0), new POI()));

        if(lon < -180.0 || 180.0 <= lon)
            throw new Exception("Longitudinal location data is out of range (-180.0, 180.0].");

        this.locationLongitude = lon;

        if(lat < -90.0 || 90.0 < lat)
            throw new Exception("Latitudinal location data is out of range [-90.0, 90.0].");

        this.locationLatitude = lat;

        setAreaWidth(areaWidth);
        setAreaHeight(areaHeight);
        addCoord(lon, lat);
        computeUnitAreaSize();
    }


    /* !@brief Sets new minimal length of unit area as a lower bound of width and height
     *         using the equatorial circumference of Earth
     *
     * @param[in] newMinAreaLength The new lower threshold in coordinate degrees */
    public static void setMinAreaLength(double newMinAreaLength) throws Exception{

        newMinAreaLength /= circPol;
        newMinAreaLength *= 360.0;

        if(newMinAreaLength < 0.0000179663)// equivalent to 2m in horizontal orientation at equator
            throw new Exception("Minimum area length is less than allowed.");

        minAreaLength = newMinAreaLength;
    }


    public double getAreaWidth(){

        return areaWidth;
    }


    public double getAreaHeight(){

        return areaHeight;
    }


    // suppose that the curvature torsion is not large enough to make visible changes in size of areas
    //  regarding the simplification of geometry of Earth as a quasi sphere
    // TODO further improvement could be the use of Haversine formula

    public void setAreaWidth(double newAreaWidth) throws Exception{


        newAreaWidth /= circEq;// * Math.abs(Math.sin(Math.toRadians(locationLatitude)));
        newAreaWidth *= 360.0;

        if(newAreaWidth < minAreaLength)
            throw new Exception("New value of area width is too small.");

        areaWidth = newAreaWidth;
    }

    public void setAreaHeight(double newAreaHeight) throws Exception{

        newAreaHeight /= circPol;
        newAreaHeight *= 360.0;

        if(newAreaHeight < minAreaLength)
            throw new Exception("New value of area height is too small.");

        areaHeight = newAreaHeight;
    }


    @Override
    public void addCoord(double lonVal, double latVal) throws Exception{

        // since the four vertices of the unit area are relatively fixed, update the central position solely
        //  and translate the boundary vertices

        if(lonVal < -180.0 || 180.0 <= lonVal)
            throw new Exception("Longitudinal location data is out of range (-180.0, 180.0].");

        this.locationLongitude = lonVal;

        if(latVal <= -90.0 || 90.0 <= latVal)
            throw new Exception("Latitudinal location data is out of range [-90.0, 90.0].");

        this.locationLatitude = latVal;

        double halfWidth = areaWidth / 2.0;
        double halfHeight = areaHeight / 2.0;

        coords.setValByInd(0, new POI(315.0, lonVal + halfWidth, latVal - halfHeight, 0));
        coords.setValByInd(1, new POI(45.0, lonVal + halfWidth, latVal + halfHeight, 0));
        coords.setValByInd(2, new POI(135.0, lonVal - halfWidth, latVal + halfHeight, 0));
        coords.setValByInd(3, new POI(225.0, lonVal - halfWidth, latVal - halfHeight, 0));
    }

    @Override
    public void computeUnitAreaSize() throws Exception {

        areaSize = areaWidth * areaHeight;

        if(areaSize < minimalArea) throw new Exception("Computed area is too small.");
    }


}
