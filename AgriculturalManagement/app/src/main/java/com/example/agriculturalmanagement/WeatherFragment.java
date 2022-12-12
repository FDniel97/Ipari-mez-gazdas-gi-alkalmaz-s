package com.example.agriculturalmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agriculturalmanagement.model.WeatherStatus;

public class WeatherFragment extends Fragment {

    WeatherStatus weatherStatus;

    private boolean isActual = true;
    private Spinner dropdown;
    private Button buttonActual;
    private Button buttonStatistic;
    //AppViewModel model = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
    //LiveData<List<Field>> fields = model.getAllFields();

    private String[] fieldNames = new String[]{"Herceghalom", "Tatabánya", " Csopak", "Szeged"};

    private String fieldName = fieldNames[0];

    private String dayStart = "2022-11-01";
    private String dayToday = "2022-12-07";

    private boolean isActual = true;
    private Spinner dropdown;
    private Button buttonActual;
    private Button buttonStatistic;

    public WeatherFragment() {
        // Required empty public constructor
    }

    //////////////////////////////////////////////////////////////////////

    private void setWeatherClassArguments()
    {
        var viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        var allFieldsLiveData = viewModel.getAllFields();
        var fields = allFieldsLiveData.getValue();

        fieldsMap = new HashMap<String, Field>();
        fieldNames = new ArrayList<String>();
        for (int i = 0; i < fields.size(); i++)
        {
            Field field = fields.get(i);
            fieldNames.add(field.getName());
            fieldsMap.put(field.getName(), field);
        }

        if (fieldNames != null)
        {
            fieldName = fieldNames.get(0);
        }

        setDates();

    }

    private void setDates()
    {
        //I will need this data from the Field objects
        //LocalDateTime dateStart = fieldsMap.get(fieldName).getDate();
        //LocalDateTime.parse needs time -> I used 04:20 randomly
        DateTimeFormatter dtfTemp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateStart = LocalDateTime.parse("2022-11-01 04:20", dtfTemp);


        LocalDateTime dateNow = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        numOfDays = (int)ChronoUnit.DAYS.between(dateStart, dateNow);

        dayStart = dateStart.format(dtf);
        dayToday = dateNow.format(dtf);
        numOfDays = 37;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setWeatherClassArguments();

    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        dropdown = rootView.findViewById(R.id.spinnerFieldsForWeather);
        initSpinnerFooter(fieldNames);

        buttonActual = (Button) rootView.findViewById(R.id.buttonActualWeather);
        buttonActual.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isActual = true;
                getWebDataAndUpdateUI(fieldsMap.get(fieldName),isActual);
            }
        });

        buttonStatistic = (Button) rootView.findViewById(R.id.buttonHistoricalWeather);
        buttonStatistic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isActual = false;
                getWebDataAndUpdateUI(fieldsMap.get(fieldName),isActual);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        //getWebDataAndUpdateUI();
    }

    @SuppressLint("DefaultLocale")
    public void getWebDataAndUpdateUI(double latitude, double longitude, boolean isActual) {

        if(isActual){
            weatherStatus.updateActualWeather(latitude, longitude, getContext());
            getActualWebDataAndUpdateUI();
        }
        else {

            weatherStatus.updateStatisticWeatherData(latitude, longitude, dayStart, dayToday, getContext());
            getStatisticWebDataAndUpdateUI();
        }
    }

    public void getActualWebDataAndUpdateUI() {

        // todo
        //  refactor naming conventions along with hidden attributes


        TextView headerText = (TextView) getView().findViewById(R.id.textviewHeader);
        TextView temperatureText = (TextView) getView().findViewById(R.id.textview1);
        TextView temperatureMinText = (TextView) getView().findViewById(R.id.textview2);
        TextView temperatureMaxText = (TextView) getView().findViewById(R.id.textview3);
        TextView precipitationText = (TextView) getView().findViewById(R.id.textview4);
        TextView radiationText = (TextView) getView().findViewById(R.id.textview5);
        TextView windspeedText = (TextView) getView().findViewById(R.id.textview6);

        headerText.setText("WEATHER TODAY");
        temperatureText.setText("Temperature now = " + weatherStatus.getTemperature() + " C˚");
        temperatureMinText.setText("Temperature min = " + weatherStatus.getTemperatureMin() + " C˚");
        temperatureMaxText.setText("Temperature max = " + weatherStatus.getTemperatureMax() + " C˚");
        precipitationText.setText("Precipitation = " + (int)weatherStatus.getPrecipitation() + " mm");
        radiationText.setText("Radiation = " + (int)weatherStatus.getRadiation() + " MJ/m2");
        windspeedText.setText("Wind speed now = " + weatherStatus.getWindspeed() + " km/h");
    }

    public void getStatisticWebDataAndUpdateUI() {

        // todo
        //  refactor naming conventions along with hidden attributes


        TextView headerText = (TextView) getView().findViewById(R.id.textviewHeader);
        TextView temperatureMinText = (TextView) getView().findViewById(R.id.textview1);
        TextView temperatureMaxText = (TextView) getView().findViewById(R.id.textview2);
        TextView precipitationText = (TextView) getView().findViewById(R.id.textview3);
        TextView precipitationHourlyText = (TextView) getView().findViewById(R.id.textview4);
        TextView radiationText = (TextView) getView().findViewById(R.id.textview5);
        TextView windspeedText = (TextView) getView().findViewById(R.id.textview6);

        headerText.setText("WEATHER STATISTICS");
        temperatureMinText.setText("Temperature min = " + weatherStatus.getTemperatureMin() + " C˚");
        temperatureMaxText.setText("Temperature max = " + weatherStatus.getTemperatureMax() + " C˚");
        precipitationText.setText("Precipitation = " + (int)weatherStatus.getPrecipitation() + " mm");
        precipitationHourlyText.setText("Precipitation in hours = " + (int)weatherStatus.getPrecipitationHours() + " h");
        radiationText.setText("Radiation = " + (int)weatherStatus.getRadiation() + " MJ/m2");
        windspeedText.setText("Wind speed max = " + weatherStatus.getWindspeed() + " km/h");
    }

    private void initSpinnerFooter(List<String> fieldNames) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, fieldNames);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Context context = getActivity();
                CharSequence text = (CharSequence) parent.getItemAtPosition(position);

                fieldName = (String)text;
                setDates();

                getWebDataAndUpdateUI(fieldsMap.get(fieldName), isActual);

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



