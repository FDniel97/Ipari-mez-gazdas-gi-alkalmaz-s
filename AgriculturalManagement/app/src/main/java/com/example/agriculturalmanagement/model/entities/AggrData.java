package com.example.agriculturalmanagement.model.entities;

import java.time.Duration;
import java.util.List;
import java.lang.Integer;
import java.lang.Double;

public class AggrData {

    private double area;
    private Duration workHours;
    private int precipitationQuantityCurr;
    private int precipitationQuantitySum;
    private int precipitationQuantityAverage;
    private List<Integer> precipitationQuantityByDayList;
    private double overcastIndexCurr;
    private double overcastIndexAverage;
    private List<Double> overcastIndexByDayList;

    public double getArea(){

        return area;
    }

    public Duration getWorkHours(){

        return workHours;
    }

    public void updatePrecipitationData(){

        // TODO...
    }

    public int getPrecipitationQuantityCurr(){

        return precipitationQuantityCurr;
    }

    public int getPrecipitationQuantitySum(){

        return precipitationQuantitySum;
    }

    public int getPrecipitationQuantityAverage(){

        return precipitationQuantityAverage;
    }

    public List<Integer> getPrecipitationQuantityByDayList(){

        return precipitationQuantityByDayList;
    }

    public double getOvercastIndexCurr(){

        return overcastIndexCurr;
    }

    public double getOvercastIndexAverage(){

        return overcastIndexAverage;
    }

    public List<Double> getOvercastIndexByDayList() {

        return overcastIndexByDayList;
    }
}
