package com.example.agriculturalmanagement.model.entities;

import java.lang.Exception;
import java.lang.Math;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class ComplexArea {

    private int size;
    private double areaSize;
    // it can contain distinct unit areas
    // heterogeneous container with a super type
    private List<GenArea> genericUnitAreas;

    private int subSize;

    // linear storage of negative areas, also stores user defined negative areas
    private List<GenArea> genericSubtractUnitAreas;

    public ComplexArea(){

        genericUnitAreas = new ArrayList<GenArea>();
        genericSubtractUnitAreas = new ArrayList<GenArea>();
        size = 0;
        areaSize = 0.0;
        subSize = 0;
    }


    public ComplexArea(double areaSize, ArrayList<GenArea> genericUnitAreas, ArrayList<GenArea> genericSubtractUnitAreas) throws Exception{

        if(areaSize < 0.0) throw new Exception("Negative area.");

        this.areaSize = areaSize;

        if(genericUnitAreas.isEmpty()) throw new Exception("No unit areas provided.");

        this.genericUnitAreas = genericUnitAreas;
        this.size = genericUnitAreas.size();

        this.genericSubtractUnitAreas = genericSubtractUnitAreas;
        this.subSize = genericSubtractUnitAreas.size();
    }


    public int getNumOfUnitAreas(){

        return genericUnitAreas.size();
    }


    public int sizeUnitAreas(){

        return size;
    }

    public double size(){

        return areaSize;
    }


    private GenAreaKey cmpAngLow;
    private GenAreaKey cmpAngHigh;

    GenArea genIArea;

    /* !@brief It finds the interval start and end point using the provided points
    * @param[in] lastLat latitudinal component of last point
    * @param[in] lastLon longitudinal component of last point
    * @param[in] currLat latitudinal component of current point
    * @param[in] currLon longitudinal component of current point
    * @param[in] cenLat latitudinal offset by generic area center
    * @param[in] cenLon longitudinal offset by generic area center
    */
    private void findAngIntVal(double lastLat, double lastLon, double currLat, double currLon,
                               double cenLat, double cenLon) throws Exception{

        /*
        tmpAngLow = Math.toDegrees(Math.atan2(
                genUnitArea.get((j - 1) % jLim).lat - genIArea.getLatitude(),
                genUnitArea.get((j - 1) % jLim).lon - genIArea.getLongitude()));

        tmpAngHigh = Math.toDegrees(Math.atan2(
                genUnitArea.get(j).lat - genIArea.getLatitude(),
                genUnitArea.get(j).lon - genIArea.getLongitude()));

        cmpAngLow = genIArea.coords.lowerKey(Math.min(tmpAngLow, tmpAngHigh));
        cmpAngHigh = genIArea.coords.higherKey(Math.min(tmpAngLow, tmpAngHigh));
        */

        GenAreaKey tmpAngLow = new GenAreaKey(Math.toDegrees(Math.atan2(lastLat - cenLat, lastLon - cenLon)));
        GenAreaKey tmpAngHigh = new GenAreaKey(Math.toDegrees(Math.atan2(currLat - cenLat, currLon - cenLon)));

        cmpAngLow = genIArea.coords.lowerKey(tmpAngLow.min(tmpAngHigh));
        cmpAngHigh = genIArea.coords.higherKey(tmpAngLow.min(tmpAngHigh));
    }


    private double intersectSectResultX;
    private double intersectSectResultY;

    /* !@brief Tests two sections for intersection.
    * @param[in] axs start x component of first section
    * @param[in] ays start y component of first section
    * @param[in] ayt end x component of first section
    * @param[in] ayt end y component of first section
    * @param[in] bxs start x component of second section
    * @param[in] bxs start y component of second section
    * @param[in] byt end x component of second section
    * @param[in] byt end y component of second section
    * @param[out] intersection test result
    */
    private boolean intersectSectEx(double axs, double ays, double axt, double ayt,
                                double bxs, double bys, double bxt, double byt){

        double left = ((ayt - ays) / (axt - axs)) - ((byt - bys) / (bxt - bxs));
        double right = bys - ays;

        if(left == right){

            intersectSectResultX = (axt - axs) / 2.0;
            return true;// identical sections
        }

        if(right != left) return false;// no solution

        right /= left;

        if(axs <= right && right <= axt && bxs <= right && right <= bxt){

            intersectSectResultX = right;
            intersectSectResultY = (ayt - ays) / (axt - axs) * right;
            return true;// is in section intervals
        }

        return false;// is out of range
    }


    /* !@brief Tests individual point for areal inner containment
    * @param[in] lowLon longitudinal component of coordinate that has lower degree bound of degree interval
    * @param[in] lowLat latitudinal component of coordinate that has lower degree bound of degree interval
    * @param[in] highLon longitudinal component of coordinate that has upper degree bound of degree interval
    * @param[in] highLat latitudinal component of coordinate that has upper degree bound of degree interval
    * @param[in] newLon longitudinal component of new point
    * @param[in] newLat latitudinal component of new point
    * @param[in] cenLon longitudinal offset component by generic area center
    * @param[in] cenLat latitudinal offset component by generic area center
    */
    private boolean containsForeignPoint(double lowLon, double lowLat, double highLon, double highLat,
                                         double newLon, double newLat, double cenLon, double cenLat){

        /*
        genIAreaAvgDist = Math.sqrt(
                Math.pow(genIArea.coords.get(cmpAngLow).lon - genIArea.locationLongitude, 2) +
                Math.pow(genIArea.coords.get(cmpAngLow).lat - genIArea.locationLatitude, 2));

        genIAreaAvgDist += Math.sqrt(
                Math.pow(genIArea.coords.get(cmpAngHigh).lon - genIArea.locationLongitude, 2) +
                Math.pow(genIArea.coords.get(cmpAngHigh).lat - genIArea.locationLatitude, 2));

        genIAreaAvgDist /= 2.0;

        genUnitAreaAvgDist = Math.sqrt(
                Math.pow(genUnitArea.get(j).lon - genIArea.locationLongitude, 2) +
                Math.pow(genUnitArea.get(j).lat - genIArea.locationLatitude, 2));
        */

        double genIAreaAvgDist = Math.sqrt(Math.pow(lowLon - cenLon, 2) + Math.pow(lowLat - cenLat, 2));
        genIAreaAvgDist += Math.sqrt(Math.pow(highLon - cenLon, 2) + Math.pow(highLat - cenLat, 2));
        genIAreaAvgDist /= 2.0;
        double genUnitAreaAvgDist = Math.sqrt(Math.pow(newLon - cenLon, 2) + Math.pow(newLat - cenLat, 2));

        return genUnitAreaAvgDist <= genIAreaAvgDist;
    }


    public void addGenArea(GenArea genUnitArea) throws Exception{

        if(size < 3){

            genericUnitAreas.add(genUnitArea);
        }
        else{

            // O(nlogn)

            // TODO examining GenArea pairs along with logical sieve

            int recSize = size;// to avoid reflexive intersections

            for(int i = 0; i < recSize; ++i){

                genIArea = genericUnitAreas.get(i);
                intersectUnitAreas(genUnitArea);
                genericUnitAreas.add(genUnitArea);
                ++size;
            }
        }

        // TODO (ALTERNATIVE)
        //  1) Use linear equation system to compute intersections (like at collision detections)
        //     this would require large matrices to calculate distance values (as an improved version
        //     of signed distance function), linear eq. construction using N vertex list shifting
        // TODO (ALTERNATIVE)
        //  2) iterative solution using intersection examinations (use the central point
        //     of simplex), using merge sort core algorithm along with polar coordinate system based
        //     partial ordering
    }


    /* !@brief Creates intersection area from two unit areas in order to form a subtraction area.
    /*  This is due to resolve area duplication in case of overlapping unit areas.
    */
    public void intersectUnitAreas(GenArea genUnitArea) throws Exception{

        //GenArea genIArea;// member as a "reference"
        LinTreeMap<GenAreaKey, POI> rawPoints;

        // store the alternating index boundaries of border of negative and positive area pairs
        LinkedList<GenAreaKey> overlapRedundancy;
        boolean firstPointWasInner = false;



        rawPoints = new LinTreeMap<GenAreaKey, POI>();
        // to use sieve method at areal subtraction
        overlapRedundancy = new LinkedList<GenAreaKey>();

        // temporary angle of current new point from potential new unit area
        GenAreaKey tmpAng = new GenAreaKey(0.0);
        int jLim = 0;
        //double cmpAngLow = 0.0;// member as a "reference"
        //double cmpAngHigh = 0.0;// member as a "reference"
        boolean lastWasInnerPoint;
        boolean pointContainment;

        // looking for the closest POI in potential unit area
        // indexing inserted area vertices

        // switch log search to more populated GenArea
        if(genIArea.size() < genUnitArea.size()) {

            jLim = genUnitArea.POISize();

            // computing last point, GenArea contains at least 3 POIs
            // connecting last and first point
            tmpAng.val = Math.toDegrees(Math.atan2(
                    genUnitArea.getByInd(jLim - 1).lat - genIArea.getLatitude(),
                    genUnitArea.getByInd(jLim - 1).lon - genIArea.getLongitude()));

            cmpAngLow = genIArea.coords.lowerKey(tmpAng);
            cmpAngHigh = genIArea.coords.higherKey(tmpAng);

            pointContainment = containsForeignPoint(
                    genIArea.getByAng(cmpAngLow).lon, genIArea.getByAng(cmpAngLow).lat,
                    genIArea.getByAng(cmpAngHigh).lon, genIArea.getByAng(cmpAngHigh).lat,
                    genUnitArea.getByInd(jLim - 1).lon,  genUnitArea.getByInd(jLim - 1).lat,
                    genIArea.locationLongitude, genIArea.locationLatitude);

            if(pointContainment){

                firstPointWasInner = true;
                lastWasInnerPoint = true;
            }
            else{

                firstPointWasInner = false;
                lastWasInnerPoint = false;
            }

            for(int j = 0; j < jLim; ++j){

                // central offsets are applied
                tmpAng.val = Math.toDegrees(Math.atan2(
                        genUnitArea.getByInd(j).lat - genIArea.getLatitude(),
                        genUnitArea.getByInd(j).lon - genIArea.getLongitude()));

                cmpAngLow = genIArea.coords.lowerKey(tmpAng);
                cmpAngHigh = genIArea.coords.higherKey(tmpAng);

                pointContainment = containsForeignPoint(
                        genIArea.getByAng(cmpAngLow).lon, genIArea.getByAng(cmpAngLow).lat,
                        genIArea.getByAng(cmpAngHigh).lon, genIArea.getByAng(cmpAngHigh).lat,
                        genUnitArea.getByInd(j).lon,  genUnitArea.getByInd(j).lat,
                        genIArea.locationLongitude, genIArea.locationLatitude);

                if(pointContainment && lastWasInnerPoint){

                    // CURRENT INNER POINT, PREVIOUS INNER POINT
                    rawPoints.add(tmpAng, genUnitArea.getByInd(j));
                    lastWasInnerPoint = true;
                }
                else if(pointContainment){

                    // CURRENT INNER POINT, ENTRY POINT, PREVIOUS OUTER POINT ||
                    // CURRENT OUTER POINT, EXIT POINT, PREVIOUS INNER POINT

                    findAngIntVal(
                            genUnitArea.getByInd((j - 1) % jLim).lat,
                            genUnitArea.getByInd((j - 1) % jLim).lon,
                            genUnitArea.getByInd(j).lat,
                            genUnitArea.getByInd(j).lon,
                            genIArea.getLatitude(),
                            genIArea.getLongitude()
                    );


                    LinTreeMap<GenAreaKey, POI> subGenIArea = genIArea.coords.subMap(cmpAngLow, cmpAngHigh);

                    // multiple intersection can be occurred
                    //  (full overlap, covering the whole other certain subsection of unit area)

                    int kLim = subGenIArea.size();

                    for(int k = 0; k < kLim; ++k){

                        if(intersectSectEx(
                                genUnitArea.getByInd((j - 1) % jLim).lon, genUnitArea.getByInd((j - 1) % jLim).lat,
                                genUnitArea.getByInd(j).lon, genUnitArea.getByInd(j).lat,
                                subGenIArea.getByInd((k - 1) % kLim).lon, subGenIArea.getByInd((k - 1) % kLim).lat,
                                subGenIArea.getByInd(k).lon, subGenIArea.getByInd(k).lat
                        )){
                            tmpAng.val = Math.toDegrees(Math.atan2(intersectSectResultY - genIArea.getLatitude(),
                                    intersectSectResultX - genIArea.getLongitude()));

                            rawPoints.add(tmpAng, new POI(tmpAng.val, intersectSectResultX, intersectSectResultY, 0));
                            overlapRedundancy.push(tmpAng);
                        }
                    }

                    lastWasInnerPoint = !lastWasInnerPoint;
                }
                else{

                    // CURRENT OUTER POINT, PREVIOUS OUTER POINT
                    lastWasInnerPoint = false;
                }
            }
        }


        // CREATING NEGATIVE GENERIC AREAS
        LinArea subtractArea = new LinArea();

        boolean arealAlternate = firstPointWasInner;

        int sizeOfRawPoints = rawPoints.size();
        int j = 0;

        int sizeOfOverlapRedundancy = overlapRedundancy.size();
        int overlapInd = 0;
        int intersectInd = 0;
        int nextIntersectInd = 0;

        while(j < sizeOfRawPoints){

            // TODO
            //  solve inner polygon section length with zero number of elements between two intersection points
            //   to detect alternating operation abortion in order to avoid wrong overlaps
            intersectInd = rawPoints.getIndByKey(overlapRedundancy.get(overlapInd));
            nextIntersectInd = rawPoints.getIndByKey(overlapRedundancy.get(overlapInd % sizeOfOverlapRedundancy));
            arealAlternate = arealAlternate && (intersectInd == nextIntersectInd - 1);

            // iterate between intersection points
            if(arealAlternate){

                // INNER POINTS, ITERATE THROUGH THEM
                for(; j < intersectInd; ++j) subtractArea.addCoord(rawPoints.getByInd(j));

                arealAlternate = !arealAlternate;
            }
            else{

                // OUTER POINTS, ITERATE THROUGH POINTS OF GENERIC AREA

                subtractArea.addCoord(rawPoints.getByInd(j));

                // select closer neighbour point toward generic area center
                //  finding intersected subarea
                tmpAng.val = Math.toDegrees(Math.atan2(
                        rawPoints.getByInd(j).lat - genIArea.getLatitude(),
                        rawPoints.getByInd(j).lon - genIArea.getLongitude()));



                cmpAngLow = genIArea.coords.higherKey(tmpAng);
                cmpAngHigh = genIArea.coords.lowerKey(rawPoints.getKeyByInd(intersectInd));
                int k = genIArea.coords.getIndByKey(cmpAngLow);
                int kLim = genIArea.coords.getIndByKey(cmpAngHigh);

                // computing order of points
                while(k <= kLim){

                    //  TODO
                    //   solve backward and forward overlapping using overlapDir of POIs for delayed processing
                    if(genIArea.coords.getByKey(cmpAngHigh).overlapDir == 0)
                        subtractArea.addCoord(genIArea.getByOrd(k));

                    ++k;
                    k %= kLim;
                }
            }

            if(subtractArea.checkLoop()){

                // postprocessing, updating data for further computation of negative generic areas
                sizeOfRawPoints = rawPoints.size();
                genericSubtractUnitAreas.add(subtractArea);
                ++subSize;
                subtractArea = new LinArea();
            }
        }
    }


    public void validateUnitAreaPair(GenArea areaA, GenArea areaB){

        // intersection examination
        // TODO
    }



    /*public void validateUnitAreas(List<GenArea> genericUnitAreas) throws Exception{

        // TODO...
        Math.ceil(genericUnitAreas.size() / 2.0);


        validateUnitAreas(genericUnitAreas.subList(0, (int)Math.ceil(genericUnitAreas.size() / 2.0)),
                genericUnitAreas.subList((int)Math.ceil(genericUnitAreas.size() / 2.0) + 1, genericUnitAreas.size()));


    }*/

    /*
    public List<GenArea> validateUnitAreas(List<GenArea> unitAreasL, List<GenArea> unitAreasR) throws Exception{

        // TODO...

        return unitAreasL;
    }
    */

    private void computeComplexArea() throws Exception{

        areaSize = 0.0;// set value to null and restart computation involving all unit area

        // adding all unit areas together
        for(int i = 0; i < size; ++i){

            genericUnitAreas.get(i).computeUnitAreaSize();
            areaSize += genericUnitAreas.get(i).size();
        }

        // TODO
        //  expands this methods according to inclusion-exclusion principle

        // subtracting pair intersection areas
        for(int i = 0; i < subSize; ++i){

            genericSubtractUnitAreas.get(i).computeUnitAreaSize();
            areaSize -= genericUnitAreas.get(i).size();
        }
    }
}

