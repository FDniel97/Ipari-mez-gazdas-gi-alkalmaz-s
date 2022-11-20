package com.example.agriculturalmanagement;

import com.example.agriculturalmanagement.model.LinTreeMap;
import java.lang.Exception;

import static org.junit.Assert.*;
import org.junit.Test;

public class LinTreeMapTests {

    @Test
    public void auxiliaryPairTests_areCorrect() throws Exception{

        // using wrappers of primitives

        // constructors
        LinTreeMap.Pair<Double, Integer> testObject;

        // default constructor
        testObject = new LinTreeMap.Pair<Double, Integer>();

        // pair initialization
        testObject = new LinTreeMap.Pair<Double, Integer>(0.1, 1);
    }


    @Test
    public void constructors_areCorrect(){

        // default constructor
        LinTreeMap<TestKey, Integer> testObject;
    }


    @Test
    public void add_methodIsCorrect() throws Exception{

        LinTreeMap<TestKey, Double> testObject = new LinTreeMap<TestKey, Double>();

        // redundancy test
        testObject.add(new TestKey(6), 6.0);

        assertEquals("The item has already been inserted earlier (redundancy is not allowed).",
                assertThrows(Exception.class, ()->{

                    testObject.add(new TestKey(6), 6.0);
                }).getMessage());


        // adding multiple elements
        /*
        testObject.add(new TestKey(9), 9.0);
        testObject.add(new TestKey(8), 8.0);
        testObject.add(new TestKey(7), 7.0);
        testObject.add(new TestKey(5), 5.0);
        testObject.add(new TestKey(4), 4.0);
        testObject.add(new TestKey(3), 3.0);
        testObject.add(new TestKey(2), 2.0);
        testObject.add(new TestKey(1), 1.0);
        */

        testObject.add(new TestKey(1), 1.0);
        testObject.add(new TestKey(3), 3.0);
        testObject.add(new TestKey(4), 4.0);
        testObject.add(new TestKey(9), 9.0);
        testObject.add(new TestKey(5), 5.0);
        testObject.add(new TestKey(2), 2.0);
        testObject.add(new TestKey(7), 7.0);


        assertEquals(0, testObject.getIndByKey(new TestKey(1)));
        assertEquals(1, testObject.getIndByKey(new TestKey(2)));
        assertEquals(2, testObject.getIndByKey(new TestKey(3)));
        assertEquals(3, testObject.getIndByKey(new TestKey(4)));


        // pair form invocation
        testObject.add(new LinTreeMap.Pair<TestKey, Double>(new TestKey(50), 6.0));
    }


    @Test
    public void getters_areCorrect() throws Exception{

        LinTreeMap<TestKey, Double> testObject = new LinTreeMap<TestKey, Double>();

        testObject.add(new TestKey(1), 2.0);
        testObject.add(new TestKey(3), 4.0);
        testObject.add(new TestKey(4), 5.0);
        testObject.add(new TestKey(2), 3.0);

        // getByInd
        assertTrue(2.0 == testObject.getByInd(0));
        assertEquals("Index out of bounds.", assertThrows(Exception.class, ()->{

            testObject.getByInd(6);
        }).getMessage());


        // getByKey
        assertTrue(4.0 == testObject.getByKey(new TestKey(3)));
        assertTrue(2.0 == testObject.getByKey(new TestKey((8))));


        // getIndByKey
        assertTrue(2 == testObject.getIndByKey(new TestKey(3)));
        assertTrue(testObject.size() - 1 == testObject.getIndByKey(new TestKey(10)));

        // getKeyByInd
        assertTrue(new TestKey(2).compareTo(testObject.getKeyByInd(1)) == 0);
        assertEquals("Index out of bounds.", assertThrows(Exception.class, ()->{

            testObject.getKeyByInd(6);
        }).getMessage());

        // size
        assertTrue(4 == testObject.size());
    }


    @Test
    public void setters_areCorrect() throws Exception{

        LinTreeMap<TestKey, String> testObject = new LinTreeMap<TestKey, String>();
        testObject.add(new TestKey(3), "three");
        testObject.add(new TestKey(4), "four");
        testObject.add(new TestKey(2), "two");

        // set key by index
        testObject.setKeyByInd(0, new TestKey(5));
        assertTrue(testObject.getKeyByInd(0).compareTo(new TestKey(5)) == 0);

        assertEquals("Index out of bounds.", assertThrows(Exception.class, ()->{

            testObject.setKeyByInd(4, new TestKey(9));
        }).getMessage());


        // set value by index
        testObject.setValByInd(2, "nine");

        assertEquals("Index out of bounds.", assertThrows(Exception.class, ()->{

            testObject.setValByInd(4, "err");
        }).getMessage());


        // set key and value by index
        testObject.setByInd(2, new TestKey(8), "five");

        assertTrue(testObject.getKeyByInd(2).compareTo(new TestKey(8)) == 0
                && testObject.getByInd(2).compareTo("five") == 0);

        assertEquals("Index out of bounds.", assertThrows(Exception.class, ()->{

            testObject.setByInd(9, new TestKey(9), "err");
        }).getMessage());
    }


    @Test
    public void gettingNearestElementsAreCorrect() throws Exception{

        LinTreeMap<TestKey, Double> testObject = new LinTreeMap<TestKey, Double>();

        // case: empty container (lowerKey)
        assertEquals("Container is empty.", assertThrows(Exception.class, ()->{

            testObject.lowerKey(new TestKey(3));
        }).getMessage());

        // case: empty container (higherKey)
        assertEquals("Container is empty.", assertThrows(Exception.class, ()->{

            testObject.lowerKey(new TestKey(3));
        }).getMessage());

        testObject.add(new TestKey(3), 3.0);
        testObject.add(new TestKey(4), 4.0);
        testObject.add(new TestKey(2), 2.0);

        // case: no lower key, returning first element from the container
        assertTrue(0 == testObject.getIndByKey(testObject.lowerKey(new TestKey(0))));

        // case: no upper key, returning last element from the container
        assertTrue(testObject.size() - 1 == testObject.getIndByKey(testObject.higherKey(new TestKey(6))));

        // case: equal element found in the container (lowerKey)
        assertTrue(new TestKey(3).compareTo(testObject.lowerKey(new TestKey(3))) == 0);

        // case: equal element found in the container (higherKey)
        assertTrue(new TestKey(3).compareTo(testObject.higherKey(new TestKey(3))) == 0);
    }


    @Test
    public void subMap_isCorrect() throws Exception{

        LinTreeMap<TestKey, Double> testObject = new LinTreeMap<TestKey, Double>();

        testObject.add(new TestKey(1), 2.0);
        testObject.add(new TestKey(3), 4.0);
        testObject.add(new TestKey(4), 5.0);
        testObject.add(new TestKey(2), 3.0);

        LinTreeMap<TestKey, Double> subTestObject = new LinTreeMap<TestKey, Double>();
        subTestObject.add(new TestKey(3), 4.0);
        subTestObject.add(new TestKey(4), 5.0);

        LinTreeMap<TestKey, Double> subTestObject2 = testObject.subMap(new TestKey(3), new TestKey(4));

        assertTrue(subTestObject.getByInd(0).compareTo(subTestObject2.getByInd(0)) == 0);
        assertTrue(subTestObject.getByInd(1).compareTo(subTestObject2.getByInd(1)) == 0);
    }
}
