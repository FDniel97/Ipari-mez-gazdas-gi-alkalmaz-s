package com.example.agriculturalmanagement;

import static org.junit.Assert.*;

import com.example.agriculturalmanagement.model.UnitArea;

import org.junit.Before;
import org.junit.Test;

import kotlin.Unit;

public class UnitAreaUnitTests {

    @Before
    public void resetStaticField() throws Exception{

        UnitArea.setMinAreaLength(2);
        UnitArea.setMinimalArea(4);
    }

    @Test
    public void constructors_areCorrect() throws Exception{

        // empty initialization
        UnitArea testObject = new UnitArea();

        // explicit initialization with width range error
        assertEquals("New value of area width is too small.",
                assertThrows(Exception.class, ()->{

            new UnitArea(0.000001, 100.0, 30.0, 40.0);
        }).getMessage());


        // explicit initialization with height range error
        assertEquals("New value of area height is too small.",
                assertThrows(Exception.class, ()->{

            new UnitArea(100.0, 0.0001, 30.0, 40.0);
        }).getMessage());


        // explicit initialization with longitude range error
        assertEquals("Longitudinal location data is out of range (-180.0, 180.0].",
                assertThrows(Exception.class, ()->{

            new UnitArea(100.0, 100.0, 300.0, 40.0);
        }).getMessage());

        assertEquals("Longitudinal location data is out of range (-180.0, 180.0].",
                assertThrows(Exception.class, ()->{

            new UnitArea(100.0, 100.0, -300.0, 40.0);
        }).getMessage());


        // explicit initialization with latitude range error
        assertEquals("Latitudinal location data is out of range [-90.0, 90.0].",
                assertThrows(Exception.class, ()->{

            new UnitArea(100.0, 100.0, 30.0, 400.0);

        }).getMessage());

        assertEquals("Latitudinal location data is out of range [-90.0, 90.0].",
                assertThrows(Exception.class, ()->{

            new UnitArea(100.0, 100.0, 30.0, -400.0);
        }).getMessage());


        // explicit initialization with size lower bound error
        UnitArea.setMinimalArea(10);
        assertEquals("Computed area is too small.",
                assertThrows(Exception.class, ()->{

            new UnitArea(3, 3, 30.0, 40.0);
        }).getMessage());


        // nominal initialization
        testObject = new UnitArea(100.0, 100.0, 30.0, 40.0);
    }


    @Test
    public void GlobalSetters_areCorrect() throws Exception{

        // minAreaLength : lower bound violation
        assertEquals("Minimum area length is less than allowed.",
                assertThrows(Exception.class, ()->{

            UnitArea.setMinAreaLength(0.0000001);
        }).getMessage());

        // minAreaLength : nominal case
        UnitArea.setMinAreaLength(10.0);


        // minimalArea : lower bound violation
        assertEquals("Minimal area size is less than allowed.",
                assertThrows(Exception.class, ()->{

            UnitArea.setMinimalArea(0.0000001);
        }).getMessage());

        // minimalArea : nominal case
        UnitArea.setMinimalArea(100.0);
    }

    @Test
    public void setters_areCorrect() throws Exception{

        UnitArea testObject = new UnitArea();

        // setAreaWidth : lower bound violation
        assertEquals("New value of area width is too small.",
                assertThrows(Exception.class, ()->{

            testObject.setAreaWidth(0.00001);
        }).getMessage());

        // setAreaWidth : nominal case
        testObject.setAreaWidth(10.0);


        // setAreaHeight : lower bound violation
        assertEquals("New value of area height is too small.",
                assertThrows(Exception.class, ()->{

            testObject.setAreaHeight(0.00001);
        }).getMessage());

        // setAreaHeight : nominal case
        testObject.setAreaHeight(10.0);


        // addCoord : out of bounds
        assertEquals("Longitudinal location data is out of range (-180.0, 180.0].",
                assertThrows(Exception.class, ()->{

            testObject.addCoord(-300.0, 10.0);
        }).getMessage());

        // addCoord : out of bounds
        assertEquals("Latitudinal location data is out of range [-90.0, 90.0].",
                assertThrows(Exception.class, ()->{

            testObject.addCoord(10.0, 100.0);
        }).getMessage());

        // addCoord : nominal case
        testObject.addCoord(10.0, 10.0);
    }

    @Test
    public void computeUnitArea_isCorrect() throws Exception{

        UnitArea testObject = new UnitArea(100.0, 100.0, 30.0, 40.0);

        UnitArea.setMinimalArea(100000);
        // area is too small (lower bound violation)
        assertEquals("Computed area is too small.", assertThrows(Exception.class, ()->{

            testObject.computeUnitAreaSize();
        }).getMessage());

        // nominal case
        UnitArea.setMinimalArea(10);
        testObject.setAreaWidth(1000);
        testObject.computeUnitAreaSize();
    }
}
