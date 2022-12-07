package com.example.agriculturalmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

//JSON
import com.example.agriculturalmanagement.model.AppViewModel;
import com.example.agriculturalmanagement.model.entities.Field;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class WeatherFragment extends Fragment {

    boolean isActual = true;
    Spinner dropdown;
    Button buttonActual;
    Button buttonStatistic;
    //AppViewModel model = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
    //LiveData<List<Field>> fields = model.getAllFields();

    String[] fieldNames = new String[]{
            "Herceghalom", "Tatabánya", " Csopak", "Szeged",
    };
    String fieldName = fieldNames[0];

    String dayStart = "2022-11-01";
    String dayToday = "2022-12-07";
    int numOfDays = 37;
    //int numOfDays = 25;

    //Feladat:
    //Bekérni a tényleges földeket
    //a neveket kiiratni a dropdownnal, azzal beállítani az értéket
    //a beállításnál átállítani a kezdődátumot és a latitude-longitude-ot



    //Generated code
    /////////////////////////////////////////////////////////////////////////
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        dropdown = rootView.findViewById(R.id.spinnerFieldsForWeather);
        initSpinnerFooter(fieldNames);

        buttonActual = (Button) rootView.findViewById(R.id.buttonActualWeather);
        buttonActual.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isActual = true;
                getWebDataAndUpdateUI(47.50,19,isActual);
            }
        });

        buttonStatistic = (Button) rootView.findViewById(R.id.buttonHistoricalWeather);
        buttonStatistic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isActual = false;
                getWebDataAndUpdateUI(47.50,19,isActual);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated (View view,
                               Bundle savedInstanceState)
    {
        //getWebDataAndUpdateUI();
    }

    @SuppressLint("DefaultLocale")
    public void getWebDataAndUpdateUI(double latitude, double longitude, boolean isActual)
    {
        String url;

        if (isActual)
        {
            url = String.format("https://api.open-meteo.com/v1/forecast?latitude=" +
                            "%f" +
                            "&longitude=" +
                            "%f" +
                            "&daily=temperature_2m_max,temperature_2m_min,precipitation_sum,windspeed_10m_max,shortwave_radiation_sum&current_weather=true&timezone=auto",
                    latitude, longitude);
            getActualWebDataAndUpdateUI(url);
        }
        else
        {
            url = String.format("https://archive-api.open-meteo.com/v1/era5?latitude=" +
                            "%f" +
                            "&longitude=" +
                            "%f" +
                            "&&start_date=" +
                            dayStart +
                            "&end_date=" +
                            dayToday +
                            "&daily=temperature_2m_max,temperature_2m_min,shortwave_radiation_sum,precipitation_sum,precipitation_hours,windspeed_10m_max&timezone=auto",
                    latitude, longitude);
            getStatisticWebDataAndUpdateUI(url);
        }
    }

    public void getActualWebDataAndUpdateUI(String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        TextView headerText = (TextView) getView().findViewById(R.id.textviewHeader);
        TextView temperatureText = (TextView) getView().findViewById(R.id.textview1);
        TextView temperatureMinText = (TextView) getView().findViewById(R.id.textview2);
        TextView temperatureMaxText = (TextView) getView().findViewById(R.id.textview3);
        TextView precipitationText = (TextView) getView().findViewById(R.id.textview4);
        TextView radiationText = (TextView) getView().findViewById(R.id.textview5);
        TextView windspeedText = (TextView) getView().findViewById(R.id.textview6);



        // Request a string response from the provided URL.
        ///////////////////////////////////////////////////////////////////////////////
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ObjectMapper mapper = new ObjectMapper();

                        try {

                            // convert JSON string to Map
                            JsonNode responseMap = mapper.readTree(response);

                            //Set texts
                            double temperature = responseMap.get("current_weather").get("temperature").asDouble();
                            double temperatureMin = responseMap.get("daily").get("temperature_2m_min").get(0).asDouble();
                            double temperatureMax = responseMap.get("daily").get("temperature_2m_max").get(0).asDouble();
                            double precipitation = responseMap.get("daily").get("precipitation_sum").get(0).asDouble();
                            double radiation = responseMap.get("daily").get("shortwave_radiation_sum").get(0).asDouble();
                            double windspeed = responseMap.get("current_weather").get("windspeed").asDouble();

                            headerText.setText("WEATHER TODAY");
                            temperatureText.setText("Temperature now = " + temperature + " C˚");
                            temperatureMinText.setText("Temperature min = " + temperatureMin + " C˚");
                            temperatureMaxText.setText("Temperature max = " + temperatureMax + " C˚");
                            precipitationText.setText("Precipitation = " + precipitation + " mm");
                            radiationText.setText("Radiation = " + radiation + " MJ/m2");
                            windspeedText.setText("Wind speed now = " + windspeed + " km/h");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                temperatureText.setText(error.toString());
            }
        });
        //////////////////////////////////////////////////////////////////////////////////


        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getStatisticWebDataAndUpdateUI(String url)
    {// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        TextView headerText = (TextView) getView().findViewById(R.id.textviewHeader);
        TextView temperatureMinText = (TextView) getView().findViewById(R.id.textview1);
        TextView temperatureMaxText = (TextView) getView().findViewById(R.id.textview2);
        TextView precipitationText = (TextView) getView().findViewById(R.id.textview3);
        TextView precipitationHourlyText = (TextView) getView().findViewById(R.id.textview4);
        TextView radiationText = (TextView) getView().findViewById(R.id.textview5);
        TextView windspeedText = (TextView) getView().findViewById(R.id.textview6);



        // Request a string response from the provided URL.
        ///////////////////////////////////////////////////////////////////////////////
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ObjectMapper mapper = new ObjectMapper();

                        try {

                            // convert JSON string to Map
                            JsonNode responseMap = mapper.readTree(response);

                            //Set texts
                            double temperatureMin = 100;
                            for (int i = 0; i < numOfDays; i++)
                            {
                                try {
                                    double temp = responseMap.get("daily").get("temperature_2m_min").get(i).asDouble();
                                    if (temperatureMin > temp)
                                    {
                                        temperatureMin = temp;
                                    }
                                }
                                finally {

                                }

                            }

                            double temperatureMax = -100;
                            for (int i = 0; i < numOfDays; i++)
                            {
                                try {
                                    double temp = responseMap.get("daily").get("temperature_2m_max").get(i).asDouble();
                                    if (temperatureMax < temp)
                                    {
                                        temperatureMax = temp;
                                    }
                                }
                                finally {

                                }
                            }

                            double windspeed = -1;
                            for (int i = 0; i < numOfDays; i++)
                            {
                                try {
                                    double temp = responseMap.get("daily").get("windspeed_10m_max").get(i).asDouble();
                                    if (windspeed < temp)
                                    {
                                        windspeed = temp;
                                    }
                                }
                                finally {

                                }

                            }

                            double precipitation = 0;
                            double precipitationHours = 0;
                            double radiation = 0;
                            for (int i = 0; i < numOfDays; i++)
                            {
                                try {
                                    precipitation += responseMap.get("daily").get("precipitation_sum").get(i).asDouble();
                                }
                                finally {

                                }
                                try {
                                    precipitationHours += responseMap.get("daily").get("precipitation_hours").get(i).asDouble();
                                }
                                finally {

                                }
                                try {
                                    radiation += responseMap.get("daily").get("shortwave_radiation_sum").get(i).asDouble();
                                }
                                finally {

                                }
                            }

                            headerText.setText("WEATHER STATISTICS");
                            temperatureMinText.setText("Temperature min = " + temperatureMin + " C˚");
                            temperatureMaxText.setText("Temperature max = " + temperatureMax + " C˚");
                            precipitationText.setText("Precipitation = " + (int)precipitation + " mm");
                            precipitationHourlyText.setText("Precipitation in hours = " + (int)precipitationHours + " h");
                            radiationText.setText("Radiation = " + (int)radiation + " MJ/m2");
                            windspeedText.setText("Wind speed max = " + windspeed + " km/h");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                temperatureMaxText.setText("That didn't work!");
            }
        });
        //////////////////////////////////////////////////////////////////////////////////


        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void initSpinnerFooter(String[] fieldNames) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, fieldNames);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Context context = getActivity();
                CharSequence text = (CharSequence) parent.getItemAtPosition(position);

                fieldName = (String)text;

                //TODO: add latitude and longitude parameters from database
                getWebDataAndUpdateUI(47.50,19, isActual);

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

}



