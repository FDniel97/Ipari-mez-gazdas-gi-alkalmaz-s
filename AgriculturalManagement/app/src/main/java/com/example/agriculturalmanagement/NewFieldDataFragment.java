package com.example.agriculturalmanagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agriculturalmanagement.model.AppViewModel;
import com.example.agriculturalmanagement.model.WeatherStatus;
import com.example.agriculturalmanagement.model.entities.ComplexArea;
import com.example.agriculturalmanagement.model.entities.Crop;
import com.example.agriculturalmanagement.model.entities.Field;
import com.example.agriculturalmanagement.model.entities.PhysicalAddress;
import com.example.agriculturalmanagement.newFieldData.AddressEditorFragment;
import com.example.agriculturalmanagement.newFieldData.FieldShapeEditor;
import com.example.agriculturalmanagement.util.ResultReceiver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarItemView;

import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import java.math.BigDecimal;

public class NewFieldDataFragment extends Fragment {

    private LiveData<List<Field>> allFields;
    private LiveData<List<Crop>> allCrops;
    private Field newField;
    private boolean validField;
    private AtomicBoolean locationGranted;
    private AppViewModel viewModel;

    private EditText name;
    private EditText workHours;
    private EditText precipitationQuantity;
    private Button selectCrop;
    private TextView creationDate;
    private TextView geoCoords;
    private TextView radiation;
    private Button editAddress;
    private TextView addressCombined;
    private Button editFieldGeometry;
    private Button saveField;
    
    Toast errorMessage;
    
    public NewFieldDataFragment() {

        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("newField", newField.toString());
        outState.putString("validField", validField + "");
        outState.putString("name", name.getText().toString());
        outState.putString("workHours", workHours.getText().toString());
        outState.putString("precipitationQuantity", precipitationQuantity.getText().toString());
        outState.putString("selectedCrop", selectCrop.getText().toString());
        outState.putString("creationDate", creationDate.getText().toString());
        outState.putString("geoCoords", geoCoords.getText().toString());
        outState.putString("radiation", radiation.getText().toString());
        outState.putString("addressCombined", addressCombined.getText().toString());
        outState.putString("fieldSize", editFieldGeometry.getText().toString());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        locationGranted = new AtomicBoolean(false);

        super.onCreate(savedInstanceState);

        try {

            newField = new Field();

        } catch (Exception e) {

            //e.printStackTrace();
            errorMessage = Toast.makeText(getContext(),
                    "Could not create empty field, try to create a new field again.", Toast.LENGTH_LONG);
            errorMessage.show();
        }

        validField = true;
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        allFields = viewModel.getAllFields();
        allCrops = viewModel.getAllCrops();
    }


    public boolean checkLocationPermission(){

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(

                new ActivityResultContracts.RequestMultiplePermissions(), result -> {

                    Boolean fineLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_FINE_LOCATION, false);

                    Boolean coarseLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if(fineLocationGranted != null && fineLocationGranted)
                        locationGranted.set(true);
                    else if(coarseLocationGranted != null && coarseLocationGranted)
                        locationGranted.set(true);
                    else
                        locationGranted.set(false);
                }
            );

            locationPermissionRequest.launch(
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
            );
        }

        return locationGranted.get();
    }


    @SuppressLint("MissingPermission")
    public Location updateGeoLocation(){

        if(checkLocationPermission()){

            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>(){

                @Override
                public void onSuccess(Location location) {

                    if(location != null) validField = true;
                }
            });

            return fusedLocationClient.getLastLocation().getResult();
        }
        else {

            Location recentLocation = new Location("defaultProvider");
            recentLocation.setLatitude(0.0);
            recentLocation.setLongitude(0.0);

            return recentLocation;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_field_data, container, false);

        validField = true;

        // UPDATING NAME INPUT FIELD
        name = rootView.findViewById(R.id.fieldName);
        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {

                name.setTextColor(Color.BLACK);

                try {
                    newField.setName(editable.toString());
                    validField = true;
                } catch (Exception e) {

                    name.setText(e.getMessage());
                    name.setTextColor(Color.RED);
                    validField = false;

                    errorMessage = Toast.makeText(getContext(),
                            "Invalid field name, try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        // UPDATING WORKHOURS INPUT FIELD
        workHours = rootView.findViewById(R.id.workHours);
        workHours.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {

                workHours.setTextColor(Color.BLACK);

                try {
                    newField.setWorkHours(Duration.ofDays(Integer.parseInt(editable.toString().trim())));
                    validField = true;
                } catch (Exception e) {

                    workHours.setTextColor(Color.RED);
                    validField = false;

                    errorMessage = Toast.makeText(getContext(),
                            "Work hours is invalid, try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        // UPDATING PRECIPITATIONQUANTITY INPUT FIELD
        precipitationQuantity = rootView.findViewById(R.id.precipitationQuantity);
        precipitationQuantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {

                precipitationQuantity.setTextColor(Color.BLACK);

                try {
                    newField.setPrecipitationQuantity(Integer.parseInt(editable.toString().trim()));
                    validField = true;
                } catch (Exception e) {

                    precipitationQuantity.setTextColor(Color.RED);
                    validField = false;
                    
                    errorMessage = Toast.makeText(getContext(),
                            "Precipitation quantity is invalid, try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        // UPDATING CROPTYPE INPUT FIELD
        selectCrop = rootView.findViewById(R.id.selectCrop);
        selectCrop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(
                        R.id.action_new_field_dest_to_crop_selector_dest);
            }
        });

        getParentFragmentManager().setFragmentResultListener("cropSelectorResult", this,
            new FragmentResultListener() {

            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                try {

                    newField.setCropType(Integer.parseInt(result.getString("cropId")));
                    selectCrop.setText("Select crop (" + result.get("cropName") + ")");
                }
                catch (Exception e) {

                    errorMessage = Toast.makeText(getContext(),
                            "Could not set crop type, try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        // UPDATING CREATION DATE
        Date currentTime = Calendar.getInstance().getTime();
        creationDate = rootView.findViewById(R.id.fieldCreatedDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);

        creationDate.setText(

                calendar.get(Calendar.YEAR) + ". " +
                calendar.get(Calendar.MONTH) + ". " +
                calendar.get(Calendar.DAY_OF_MONTH) + ". " +
                calendar.get(Calendar.HOUR) + ":" +
                calendar.get(Calendar.MINUTE)
        );

        newField.setCreationDate(currentTime);

        // UPDATING LOCATION DEPENDENT VALUES
        geoCoords = rootView.findViewById(R.id.geoCoords);

        if(checkLocationPermission()){

            // precise or approximate location access granted
            geoCoords.setTextColor(Color.BLACK);
            geoCoords.setText("");
            validField = true;
        }
        else{

            // no location access granted
            geoCoords.setTextColor(Color.RED);
            geoCoords.setText("Could not access location.");
            validField = false;
        }

        Location recentLocation = updateGeoLocation();

        try {

            newField.setLocationLatitude(recentLocation.getLatitude());
            newField.setLocationLongitude(recentLocation.getLongitude());

            geoCoords.setTextColor(Color.BLACK);
            geoCoords.setText(recentLocation.getLatitude() + " " + recentLocation.getLongitude());
        }
        catch (Exception e) {

            geoCoords.setTextColor(Color.RED);
            //geoCoords.setText("Location does not exist.");
            //e.printStackTrace();
            errorMessage = Toast.makeText(getContext(),
                    "Location does not exist, try to detect location again.", Toast.LENGTH_LONG);
            errorMessage.show();
        }

        // UPDATING WEATHER CONDITIONS
        WeatherStatus weatherStatus = new WeatherStatus();
        weatherStatus.updateActualWeather(recentLocation.getLatitude(), recentLocation.getLongitude(), getContext());

        radiation = rootView.findViewById(R.id.radiation);
        radiation.setText("Radiation " + weatherStatus.getRadiation() + "MJ/m2");

        // UPDATING PHYSICAL ADDRESS (POSTAL CODE AND PARCEL NUMBER)
        editAddress = rootView.findViewById(R.id.editAddress);
        editAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(
                        R.id.action_new_field_dest_to_address_editor_dest);
            }
        });
        // todo unresolved logical dependency of (physical address-parcel number, geo coordinate) pair
        //  needs to be implemented

        addressCombined = rootView.findViewById(R.id.addressCombined);

        getParentFragmentManager().setFragmentResultListener("addressEditorResult", this,
            new FragmentResultListener() {

            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                try {

                    PhysicalAddress physicalAddress = new PhysicalAddress();
                    physicalAddress.fromString(result.getString("address"));
                    newField.setPhysicalAddress(physicalAddress);

                    addressCombined.setText(physicalAddress.getZipCode() + " " + physicalAddress.getCountry() + "\n" +
                            physicalAddress.getMunicipality() + "\n" +
                            physicalAddress.getCity() + "\n" +
                            physicalAddress.getDistrict() + "\n" +
                            physicalAddress.getStreet() + " " + physicalAddress.getNumber() + "\n" +
                            physicalAddress.getParcelNumber());
                }
                catch (Exception e) {

                    errorMessage = Toast.makeText(getContext(),
                            "Ill conditioned address, try to edit address again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        // UPDATING FIELD PHYSICAL GEOMETRY
        editFieldGeometry = rootView.findViewById(R.id.fieldGeometryEdit);
        ComplexArea fieldGeometry = new ComplexArea();

        // TODO solve return value of ComplexArea in update
        editFieldGeometry.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View view) {

                 Navigation.findNavController(view).navigate(
                         R.id.action_new_field_dest_to_field_shape_editor_dest);
             }
         });

        getParentFragmentManager().setFragmentResultListener("shapeEditorResult", this,
            new FragmentResultListener() {

            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                try {

                    ComplexArea complexArea= new ComplexArea();
                    complexArea.fromString(result.getString("shape"));
                    newField.setPhysicalGeometry(complexArea);
                    geoCoords.setText(complexArea.getLatitude() + " Lat\n " + complexArea.getLongitude() + " Lon");
                    editFieldGeometry.setText("Edit field geometry (" +
                            new BigDecimal(complexArea.getAreaSize() * 100000000).setScale(4, RoundingMode.HALF_UP) + " m^2)");
                }
                catch (Exception e) {

                    errorMessage = Toast.makeText(getContext(),
                            "Ill conditioned physical geometry, try to edit address again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        if(validField){

            try {

                newField.setLocationLongitude(fieldGeometry.getLongitude());
                newField.setLocationLatitude(fieldGeometry.getLatitude());
            }
            catch (Exception e) {

                //e.printStackTrace();
                // invalid coordinate, try again
                // todo error message pop up box should be used
                errorMessage = Toast.makeText(getContext(),
                        "Invalid coordinate, try to add new complex area again.", Toast.LENGTH_LONG);
                errorMessage.show();
            }
        }

        // APPLYING CHANGES AND PERSIST DATA IN DATABASE
        saveField = rootView.findViewById(R.id.saveField);
        saveField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // todo test case: always true
                validField = true;
                if(validField){

                    var resultReceiver = new ResultReceiver<Void>() {
                        @Override
                        public void onSuccess(Void value) {
                            Toast.makeText(requireContext(), R.string.success, Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).popBackStack();
                        }

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        }
                    };

                    viewModel.insertField(newField, resultReceiver);
                    Navigation.findNavController(view).popBackStack();
                }
                else{

                    errorMessage = Toast.makeText(getContext(),
                            "Invalid field, fix errors and try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        if(savedInstanceState != null){

            try {

                newField.fromString(savedInstanceState.getString("newField"));
            }
            catch (Exception e) {

                e.printStackTrace();
            }
            validField = Boolean.parseBoolean(savedInstanceState.getString("validField"));
            name.setText(savedInstanceState.getString("name"));
            workHours.setText(savedInstanceState.getString("workHours"));
            precipitationQuantity.setText(savedInstanceState.getString("precipitationQuantity"));
            selectCrop.setText(savedInstanceState.getString("selectedCrop"));
            creationDate.setText(savedInstanceState.getString("creationDate"));
            geoCoords.setText(savedInstanceState.getString("geoCoords"));
            radiation.setText(savedInstanceState.getString("radiation"));
            addressCombined.setText(savedInstanceState.getString("addressCombined"));
            editFieldGeometry.setText(savedInstanceState.getString("fieldSize"));
        }

        return rootView;
    }
}
