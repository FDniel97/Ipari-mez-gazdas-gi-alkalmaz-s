package com.example.agriculturalmanagement.model.entities;

import java.lang.Exception;
import java.lang.Math;
import java.util.List;

public class ComplexArea {

    private List<GenArea> genericUnitAreas;// heterogeneous container with a super type

    public ComplexArea(List<GenArea> genericUnitAreas) throws Exception{

        if(genericUnitAreas.isEmpty()) throw new Exception("No unit areas provided.");

        // TODO...

        // IDEA: USE MERGE SORT LIKE RECURSE ALGORITHM: the possibility of the implementation
        //  strongly depends on the partial ordering relation (whether the elements have this property or not)
        //  Multidimensional(2D) recursive evaluation

        // validating each unit area

        // individual internal attribute error handling in unit area classes
        // checking integrity of area vicinity
        validateUnitAreas(genericUnitAreas);

    }

    public int getNumOfUnitAres(){

        return genericUnitAreas.size();
    }

    public double size(){

        // TODO...

        return 0.0;
    }

    public void validateUnitAreas(List<GenArea> genericUnitAreas) throws Exception{

        // TODO...
        Math.ceil(genericUnitAreas.size() / 2.0);


        validateUnitAreas(genericUnitAreas.subList(0, (int)Math.ceil(genericUnitAreas.size() / 2.0)),
                genericUnitAreas.subList((int)Math.ceil(genericUnitAreas.size() / 2.0) + 1, genericUnitAreas.size()));
    }

    public List<GenArea> validateUnitAreas(List<GenArea> unitAreasL, List<GenArea> unitAreasR) throws Exception{

        // TODO...

        return unitAreasL;
    }

    public void addGenArea(GenArea genericUnitArea){

        // adding only one area to the unit area list
        // TODO...
    }
}
