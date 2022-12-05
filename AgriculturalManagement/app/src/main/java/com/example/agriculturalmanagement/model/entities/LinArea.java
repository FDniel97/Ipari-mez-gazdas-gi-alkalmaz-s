package com.example.agriculturalmanagement.model.entities;

import java.lang.String;
import java.lang.Exception;
import java.lang.Math;

public class LinArea extends GenArea{

    private boolean initialized = false;
    private static double equivalenceThreshold = 0.2;// in unit value
    private String rawCoords;// serialized form of POIs in order to reduce complexity of database

    public LinArea(){

        super();

        locationLongitude = 0.0;
        locationLatitude = 0.0;
        areaSize = 0.0;
    }

    public LinArea(double lon, double lat, String rawCoords) throws Exception{

        super();

        initialized = false;
        this.locationLongitude = lon;
        this.locationLatitude = lat;

        this.rawCoords = rawCoords;
        initArea();
    }

    public void initArea() throws Exception{

        if(initialized) throw new Exception("Area has already been initialized.");

        if(rawCoords.isEmpty()) throw new Exception("Raw coordinate data is empty.");

        String[] rawCoordsArr = rawCoords.split(">", rawCoords.length());

        int sizeOfRawCoordsArr = rawCoordsArr.length;

        if(sizeOfRawCoordsArr == 1) throw new Exception("Ill conditioned coordinates.");

        if(sizeOfRawCoordsArr == 2)
            throw new Exception("Not enough coordinates to create linear area.");

        POI poi;

        try{

            for(int i = 0; i < sizeOfRawCoordsArr; ++i){

                poi = new POI(rawCoordsArr[i]);
                coords.add(new LinTreeMap.Pair<GenAreaKey, POI>(new GenAreaKey(poi.ang), poi));
            }
        }
        catch (Exception e){

            throw new Exception("Error at creating poi. " + e.getMessage());
        }

        initialized = true;
    }


    public static void setEquivalenceThreshold(double newThreshold) throws Exception{

        if(newThreshold < 0.0001) throw new Exception("New equivalence threshold is too small.");

        equivalenceThreshold = newThreshold;
    }


    public static void setMinimalArea(double newMinimum) throws Exception{

        if(newMinimum < 0.0001) throw new Exception("New minimum size of area is too small.");

        minimalArea = newMinimum;
    }


    public boolean checkLoop() throws Exception{

        if(coords.size() > 2) return Math.sqrt(
                Math.pow(coords.getByInd(coords.size() - 1).lon - coords.getByInd(0).lon, 2)
                + Math.pow(coords.getByInd(coords.size() - 1).lat - coords.getByInd(0).lat, 2)) > equivalenceThreshold;
        else return false;
    }


    // coordinates are in order of sequence not order of naming system
    // 0 area is not possible due to condition of vicinity points
    public static double computeSimplexArea(double ax, double ay, double bx, double by, double cx, double cy){

        // with translation correction
        //return Math.abs((bx - ax) * (cy - by) - (ay) * (cx)) / 2.0;
        return Math.abs((cx - ax) * (by - ay) - (cy - ay) * (bx - ax)) / 2.0;
    }


    // TODO: add option non-convex divergent geometry construction

    /* adding coordinate to existing list of coords
    *  in case of empty coord list, the method returns false whilst the constructed geometry
    *  is inconsistent (zero area, overlapping) */
    public void addCoord(POI newPoint) throws Exception{

        addCoord(newPoint.lon, newPoint.lat);
    }


    /* !@brief Value conditioning based on [0, 360.0) interval

    * @param[in] ang Angle to be conditioned */
    private static double angCond(double ang){

        if(ang < 0) return ang + 360.0;
        else if(ang >= 360.0) return ang - 360.0;
        else return ang;
    }

    private void updatePosAngles() throws Exception{

        int size = coords.size();
        POI poi;

        for(int i = 0; i < size; ++i){

            poi = coords.getByInd(i);
            double ang = angCond(Math.toDegrees(Math.atan2(poi.lat - locationLatitude, poi.lon - locationLongitude)));
            poi.ang = ang;
            coords.setByInd(i, new GenAreaKey(ang), poi);
            System.out.println(coords.getKeyByInd(i).val + "??" + coords.getByInd(i).ang);
        }

        coords.sort();
        System.out.println("((((((((((((");
        for(int i = 0; i < size; ++i){

            System.out.println(coords.getKeyByInd(i).val + "!!" + coords.getByInd(i).ang);
        }
    }

    @Override
    public void addCoord(double lonVal, double latVal) throws Exception{

        // redundancy check
        POI currPoi;
        int size = coords.size();

        for(int i = 0; i < size; ++i){

            currPoi = coords.getByInd(i);

            if(currPoi.lon == lonVal && currPoi.lat == latVal)
                throw new Exception("The desired coordinate has already been added to the list.");
        }

        double dist = 0.0;

        // vicinity distance check
        for(int i = 0; i < size; ++i){

            dist = Math.sqrt(Math.pow(lonVal - coords.getByInd(i).lon, 2) + Math.pow(latVal - coords.getByInd(i).lat, 2));

            if(dist < equivalenceThreshold)
                throw new Exception("Given coordinate is close to one of existing coordinate.");
        }

        // unity check (assumption: the geometry is defined by a graph route)
        // the boundary traversal is sequentially successive, hence the center point and the angle
        //  of points are modified at each insert
        if(size == 0){

            locationLongitude = lonVal;
            locationLatitude = latVal;

            coords.add(new GenAreaKey(0.0), new POI(0.0, lonVal, latVal, 0));
            coordOrder.add(0, 0);
            ++size;
        }
        else if(size == 1){

            locationLongitude += lonVal / (double)(size);
            locationLongitude /= (double)(size + 1);
            locationLatitude += latVal / (double)(size);
            locationLatitude /= (double)(size + 1);

            double currAngle = angCond(Math.toDegrees(Math.atan2(latVal - locationLatitude, lonVal - locationLongitude)));

            coords.add(new GenAreaKey(currAngle), new POI(currAngle, lonVal, latVal, 0));
            coordOrder.add(1, 1);
            ++size;
            updatePosAngles();

        }
        else if(size >= 2){

            // insert point at the end of the list
            locationLongitude += lonVal / (double)(size);
            locationLongitude /= (double)(size + 1);
            locationLatitude += latVal / (double)(size);
            locationLatitude /= (double)(size + 1);

            double currAngle = angCond(Math.toDegrees(Math.atan2(latVal - locationLatitude, lonVal - locationLongitude)));

            double existingPointDist = 0.0;
            double currPointDist = 0.0;
            double primSize = 0.0;
            int overlapDir = 0;

            // Algorithm brief explanation
            //  The aim is to restrict the unit area creation by the oscillation of points around a
            //  central point. Using the computed average point from coordinates, examine the
            //  coordinates by polar system based angle orientation. If the subsequent points fluctuate
            //  in different angle regions (using the atan2 function and the signed distance function),
            //  then the geometry must be split into two subareas.
            // Inserting into ordered (manually) array list

            for(int i = size - 1; i >= 1; --i) {

                // overlap occurs
                if (coords.getByInd(i).ang >= currAngle) {

                    currPointDist = Math.sqrt(Math.pow(lonVal - locationLongitude, 2)
                            + Math.pow(latVal - locationLatitude, 2));

                    existingPointDist = Math.sqrt(Math.pow(coords.getByInd(i).lon - locationLongitude, 2)
                            + Math.pow(coords.getByInd(i).lat - locationLatitude, 2));

                    existingPointDist += Math.sqrt(Math.pow(coords.getByInd((i - 1) % size).lon - locationLongitude, 2)
                            + Math.pow(coords.getByInd((i - 1) % size).lat - locationLatitude, 2));

                    existingPointDist /= 2.0;

                    if (currPointDist - existingPointDist < 0.0) {

                        primSize = computeSimplexArea(
                                coords.getByInd((i - 1) % size).lon,
                                coords.getByInd((i - 1) % size).lat,
                                lonVal,
                                latVal,
                                coords.getByInd(i).lat,
                                coords.getByInd(i).lon
                        );


                        if (primSize < minimalArea) {
                            throw new Exception("A small part of area is not " +
                                    "large enough to record, please select another point.");
                        }

                        overlapDir = (-1) * (size - i);
                    } else {
                        // intersection occurs
                        throw new Exception("The current section intersects one of the previous "
                                + "sections, please select another point.");
                    }

                    break;
                }
                // forward insertion is not allowed because of unknown future points,
                //  maybe priori overlapInd scheduling should be used
                /* else if(coords.getByInd(i).ang < currAngle){


                }
                */
            }

            coords.add(new GenAreaKey(currAngle), new POI(currAngle, lonVal, latVal, overlapDir));
            coordOrder.add((size + overlapDir) % size, size - 1);
            ++size;
            updatePosAngles();
        }
    }


    @Override
    public void computeUnitAreaSize() throws Exception{

        int size = coords.size();

        if(size < 3) throw new Exception("Number of POIs is too small to compute area "
                + "(Size must be greater than 2).");

        if(checkLoop()) throw new Exception("Area border is not closed.");

        areaSize = 0.0;// recalculating area

        for(int i = 0; i < size; ++i){

            if(coords.getByInd(i).overlapDir % 2 == -1){

                // convergent area subtraction due to overlapping
                areaSize -= computeSimplexArea(
                        coords.getByInd((i + coords.getByInd(i).overlapDir) % size).lon,
                        coords.getByInd((i + coords.getByInd(i).overlapDir) % size).lat,
                        coords.getByInd(i).lon,
                        coords.getByInd(i).lat,
                        coords.getByInd((i - 1) % size).lon,
                        coords.getByInd((i - 1) % size).lat
                );
            }
            /*else if(coords.get(i).overlapDir % 2 == 1){

                // divergent area subtraction due to overlapping
                // TODO...
            }*/
            else{
                areaSize += computeSimplexArea(
                        coords.getByInd(i).lat,
                        coords.getByInd(i).lon,
                        locationLongitude,
                        locationLatitude,
                        coords.getByInd((i + 1) % size).lon,
                        coords.getByInd((i + 1) % size).lat
                );
            }
        }

        if(areaSize < 0) throw new Exception("Computed area is negative.");

        if(areaSize < minimalArea) throw new Exception("Computed area is smaller than minimum allowed area.");



        // TODO...
        //  IDEA: looking for infimum and supremum values
        //  first compute the inner rectangle after the smaller rectangles and after these the half rectangles/triangles
        //  1) inner_rect = (sup{X} - inf{X}) * (sup{Y} - inf{Y})
        //  2) compute steepness tendency, looking for line with more than 2 unit values
        //  3) take geometric cuts to subtract the compute rectangle
        //  4) repair the open subareas
        //  5) goto step 1 with tail recursive procedure, calling multiple subarea computation methods parallel
    }

    public GenAreaKey getLowerAng(GenAreaKey key) throws Exception{

        return coords.lowerKey(key);
    }
}
