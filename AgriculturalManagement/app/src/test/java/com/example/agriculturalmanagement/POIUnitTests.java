package com.example.agriculturalmanagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import org.junit.Test;

import com.example.agriculturalmanagement.model.POI;


public class POIUnitTests {

    private Exception intervalExaminations(double ang, double lon, double lat, int overlapDir) {

        return assertThrows(Exception.class, ()->{

            new POI(ang, lon, lat, overlapDir);
        });
    }

    @Test
    public void POIAuxiliaryClass_isCorrect(){

        // empty initialization
        POI testObject = new POI();

        // empty literal data
        assertEquals("No appropriate data has provided.", assertThrows(Exception.class, ()->{

            new POI("");

        }).getMessage());

        // inappropriate data
        assertEquals("No appropriate data has provided.", assertThrows(Exception.class, ()->{

            new POI("error@");

        }).getMessage());

        // ill assigned literal data (Double parser exception)
        assertEquals("An error has occurred during parsing numeral values.", assertThrows(Exception.class, ()->{

            new POI("12.34@err@56.78@12");

        }).getMessage());

        // ill assigned literal data (Integer parser exception)
        assertEquals("An error has occurred during parsing numeral values.", assertThrows(Exception.class, ()->{

            new POI("12.34@11.11@56.78@err");

        }).getMessage());

        // explicit parameter passes (interval errors)
        assertEquals("Orientation identifier is out of range [0.0, 360.0).",
                intervalExaminations(-1.0, 0.0, 0.0, 0).getMessage());

        assertEquals("Orientation identifier is out of range [0.0, 360.0).",
                intervalExaminations(380.0, 0.0, 0.0, 0).getMessage());

        assertEquals("Longitudinal location data is out of range (-180.0, 180.0].",
                intervalExaminations(0.0, -190.0, 0.0, 0).getMessage());

        assertEquals("Longitudinal location data is out of range (-180.0, 180.0].",
                intervalExaminations(0.0, 190.0, 0.0, 0).getMessage());

        assertEquals("Latitudinal location data is out of range [-90.0, 90.0].",
                intervalExaminations(0.0, 0.0, -100.0, 0).getMessage());

        assertEquals("Latitudinal location data is out of range [-90.0, 90.0].",
                intervalExaminations(0.0, 0.0, 100.0, 0).getMessage());

        // nominal case
        try{
            testObject = new POI(0.0, 0.0, 0.0, 0);
        }
        catch(Exception e){

            fail("An error has occurred during nominal case of constructor call (" + e.getMessage() + ").");
        }
    }

}
