package com.example.agriculturalmanagement.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class WeatherStatus {

    private double temperature = 0.0;
    private double temperatureMin = 0.0;
    private double temperatureMax = 0.0;
    private double precipitation = 0.0;
    private double radiation = 0.0;
    private double windspeed = 0.0;
    private double precipitationHours = 0.0;

    private int numOfDays = 37;
    //private int numOfDays = 25;


    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public double getRadiation() {
        return radiation;
    }

    public void setRadiation(double radiation) {
        this.radiation = radiation;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }

    public double getPrecipitationHours(){

        return precipitationHours;
    }

    public void setPrecipitationHours(double precipitationHours){

        this.precipitationHours = precipitationHours;
    }


    public void updateActualWeather(double latitude, double longitude, Context context){

        String url = String.format("https://api.open-meteo.com/v1/forecast?" +
            "latitude=" +
            "%f" +
            "&longitude=" +
            "%f" +
            "&daily=temperature_2m_max," + "temperature_2m_min," + "precipitation_sum," +
            "windspeed_10m_max," +
            "shortwave_radiation_sum" +
            "&current_weather=true" +
            "&timezone=auto",
            latitude, longitude
        );

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    ObjectMapper mapper = new ObjectMapper();

                    try {

                        // convert JSON string to Map
                        JsonNode responseMap = mapper.readTree(response);

                        //Set texts
                        temperature = responseMap.get("current_weather").get("temperature").asDouble();
                        temperatureMin = responseMap.get("daily").get("temperature_2m_min").get(0).asDouble();
                        temperatureMax = responseMap.get("daily").get("temperature_2m_max").get(0).asDouble();
                        precipitation = responseMap.get("daily").get("precipitation_sum").get(0).asDouble();
                        radiation = responseMap.get("daily").get("shortwave_radiation_sum").get(0).asDouble();
                        windspeed = responseMap.get("current_weather").get("windspeed").asDouble();

                    }
                    catch (IOException e) {

                        temperature = Double.NaN;
                        temperatureMin = Double.NaN;
                        temperatureMax = Double.NaN;
                        precipitation = Double.NaN;
                        radiation = Double.NaN;
                        windspeed = Double.NaN;
                        //e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    temperature = Double.NaN;
                    temperatureMin = Double.NaN;
                    temperatureMax = Double.NaN;
                    precipitation = Double.NaN;
                    radiation = Double.NaN;
                    windspeed = Double.NaN;

                    //error.printStackTrace();
                }
            }
        );

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void updateStatisticWeatherData(double latitude, double longitude, String dayStart, String dayToday, Context context){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = String.format("https://archive-api.open-meteo.com/v1/era5?" +
            "latitude=" +
            "%f" +
            "&longitude=" +
            "%f" +
            "&&start_date=" +
            dayStart +
            "&end_date=" +
            dayToday +
            "&daily=" +
                "temperature_2m_max," +
                "temperature_2m_min," +
                "shortwave_radiation_sum," +
                "precipitation_sum," +
                "precipitation_hours," +
                "windspeed_10m_max" +
            "&timezone=auto",
            latitude, longitude
        );

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    ObjectMapper mapper = new ObjectMapper();

                    try {

                        // convert JSON string to Map
                        JsonNode responseMap = mapper.readTree(response);

                        //Set texts
                        for (int i = 0; i < numOfDays; i++) {

                            try {

                                double val = responseMap.get("daily").get("temperature_2m_min").get(i).asDouble();

                                if (temperatureMin > val && !Double.isNaN(val)) temperatureMin = val;

                                val = responseMap.get("daily").get("temperature_2m_max").get(i).asDouble();

                                if (temperatureMax < val && !Double.isNaN(val)) temperatureMax = val;

                                windspeed = -1;
                                val = responseMap.get("daily").get("windspeed_10m_max").get(i).asDouble();

                                if (windspeed < val && !Double.isNaN(val)) windspeed = val;

                                precipitation += responseMap.get("daily").get("precipitation_sum").get(i).asDouble();

                                precipitationHours += responseMap.get("daily").get("precipitation_hours").get(i).asDouble();

                                radiation += responseMap.get("daily").get("shortwave_radiation_sum").get(i).asDouble();
                            }
                            finally {

                            }
                        }
                    }
                    catch (IOException e) {

                        temperatureMin = Double.NaN;
                        temperatureMax = Double.NaN;
                        windspeed = Double.NaN;
                        precipitation = Double.NaN;
                        precipitationHours = Double.NaN;
                        radiation = Double.NaN;

                        // todo throw
                        //e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    temperatureMin = Double.NaN;
                    temperatureMax = Double.NaN;
                    windspeed = Double.NaN;
                    precipitation = Double.NaN;
                    precipitationHours = Double.NaN;
                    radiation = Double.NaN;
                    //temperatureMaxText.setText("That didn't work!");

                    // todo throw
                }
            }
        );

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
