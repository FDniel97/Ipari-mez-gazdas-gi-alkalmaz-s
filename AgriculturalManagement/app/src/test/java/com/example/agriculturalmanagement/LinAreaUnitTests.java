package com.example.agriculturalmanagement;

import static org.junit.Assert.*;
import java.lang.String;

import com.example.agriculturalmanagement.model.entities.LinArea;
import com.example.agriculturalmanagement.model.entities.POI;

import org.junit.Test;

public class LinAreaUnitTests {

    @Test
    public void constructors_areCorrect() throws Exception{

        // empty initialization
        LinArea testObject = new LinArea();


        // literal initialization with inappropriate number of parameters
        // empty literal
        assertEquals("Raw coordinate data is empty.", assertThrows(Exception.class, ()->{

            new LinArea(0.0, 0.0, "");
        }).getMessage());

        // wrong delimiter character
        assertEquals("Ill conditioned coordinates.",
                assertThrows(Exception.class, ()->{

            new LinArea(0.0, 0.0, "1.0@1.0@1.0" + "$" + "1$2.0@2.0@2.0@2" + "$" + "3.0@3.0@3.0@3");
        }).getMessage());

        // not enough data
        assertEquals("Not enough coordinates to create linear area.", assertThrows(Exception.class, ()->{

            new LinArea(0.0, 0.0, "1.0@1.0@1.0@1" + ">" + "2.0@2.0@2.0@2");
        }).getMessage());

        // ill defined coordinates
        assertEquals("Error at creating poi. No appropriate data has provided.", assertThrows(Exception.class, ()->{

            new LinArea(0.0, 0.0, "1.0@1.0@1" + ">" + "2.0@2.0@2.0@2" + ">" + "3.0@3.0@3.0@3");
        }).getMessage());

        // literal initialization with double parse error
        assertEquals("Error at creating poi. An error has occurred during parsing numeral values.",
                assertThrows(Exception.class, ()->{

            new LinArea(0.0, 0.0, "1.0@err@1.0@1" + ">" + "2.0@2.0@2.0@2" + ">" + "3.0@3.0@3.0@3");
        }).getMessage());

        // literal initialization with int parse error
        assertEquals("Error at creating poi. An error has occurred during parsing numeral values.",
                assertThrows(Exception.class, ()->{

            new LinArea(0.0, 0.0, "1.0@1.0@1.0@err" + ">" + "2.0@2.0@2.0@2" + ">" + "3.0@3.0@3.0@3");
        }).getMessage());

        // nominal case
        try{

            testObject = new LinArea(0.0, 0.0, "1.0@1.0@1.0@0" + ">" + "2.0@2.0@2.0@0" + ">" + "3.0@3.0@3.0@0");
        }
        catch(Exception e){

            fail("An error has occurred during nominal case of constructor call.");
        }

        final LinArea testObject2 = new LinArea(0.0, 0.0, "1.0@1.0@1.0@0" + ">" + "2.0@2.0@2.0@0" + ">" + "3.0@3.0@3.0@0");
        assertEquals("Area has already been initialized.", assertThrows(Exception.class, ()->{

            testObject2.initArea();
        }).getMessage());
    }


    @Test
    public void globalConditionSetters_areCorrect(){

        // equivalenceThreshold : under lower bound
        assertEquals("New equivalence threshold is too small.", assertThrows(Exception.class, ()->{

            LinArea.setEquivalenceThreshold(-0.001);
        }).getMessage());

        // equivalenceThreshold : nominal case
        try{

            LinArea.setEquivalenceThreshold(0.1);
        }
        catch(Exception e){

            fail("An error has occurred during nominal case of constructor call.");
        }

        // minimalArea : under lower bound
        assertEquals("New minimum size of area is too small.", assertThrows(Exception.class, ()->{

            LinArea.setMinimalArea(-1.0);
        }).getMessage());

        // minimalArea : nominal case
        try{

            LinArea.setMinimalArea(1.0);
        }
        catch(Exception e) {

            fail("An error has occurred during nominal case of constructor call.");
        }
    }


    @Test
    public void simplexAreaComputation_isCorrect(){

        // triangle area computation

        // (0.0, 0.0), (0.0, 1.0), (1.0, 0.0)
        assertTrue(0.5 == LinArea.computeSimplexArea(
                0.0, 0.0, 0.0, 1.0, 1.0, 0.0));

        // (0.0, 0.0), (0.0, 1.0), (1.0, 1.0)
        assertTrue(0.5 == LinArea.computeSimplexArea(
                0.0, 0.0, 0.0, 1.0, 1.0, 1.0));

        // (0.0, 0.0), (2.0, -1.0), (8.0, 7.0)
        assertTrue(11.0 == LinArea.computeSimplexArea(
                0.0, 0.0, 2.0, -1.0, 8.0, 7.0));

        // (-3.0, 4.0), (-1.0, 3.0), (5.0, 11.0)
        assertTrue(11.0 == LinArea.computeSimplexArea(
                -3.0, 4.0, -1.0, 3.0, 5.0, 11.0));
    }


    @Test
    public void addCoord_isCorrect() throws Exception{
/*
        LinArea testObject = new LinArea();

        testObject.addCoord(new POI(0.0, 1.0, 1.0, 0));


        // redundancy test (exact equivalency)
        assertEquals("The desired coordinate has already been added to the list.",
                assertThrows(Exception.class, ()->{

            testObject.addCoord(new POI(0.0, 1.0, 1.0, 0));
        }).getMessage());


        // redundancy test (loose equivalency), too close neighbor coordinate
        assertEquals("Given coordinate is close to one of existing coordinate.", assertThrows(Exception.class, ()->{

            testObject.addCoord(0.99, 0.99);
        }).getMessage());


        // a simplex intersection of area is too small (subsequent examination for convergent overlap)
        LinArea.setMinimalArea(100.0);
        testObject.addCoord(3.5, 2.0);
        assertEquals("A small part of area is not large enough to record, please select another point.",
                assertThrows(Exception.class, ()->{

            testObject.addCoord(1.0, 0.66);
        }).getMessage());

        // nominal case of overlapping simplex area size
        LinArea.setMinimalArea(0.01);

*/
        LinArea testObject2 = new LinArea();
        testObject2.addCoord(1.0, 1.0);
        testObject2.addCoord(3.5, 2.0);
        testObject2.addCoord(1.0, 0.66);

        System.out.println("a");
        // potential new section cuts a previously created section
        /*
        assertEquals("The current section intersects one of the previous sections, please select another point.", assertThrows(Exception.class, ()->{

            testObject2.addCoord(-80.0, -80.0);
        }).getMessage());
        */


        // nominal case
    }



    @Test
    public void unitAreaComputation_isCorrect(){

        // number of POIs is less than 3
        LinArea testObject = new LinArea();







        // geometry validation for closure



        // area is less than minimimal allowed area size


        // area is negative


        // nominal case


    }


    @Test
    public void serialization_isCorrect(){

        // nominal case
        try{


        }
        catch(Exception e){

            fail("An error has occurred during case of raw data construction.");
        }
    }
}
