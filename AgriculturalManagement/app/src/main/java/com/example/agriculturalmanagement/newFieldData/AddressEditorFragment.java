package com.example.agriculturalmanagement.newFieldData;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agriculturalmanagement.CalendarFragment;
import com.example.agriculturalmanagement.NewFieldDataFragment;
import com.example.agriculturalmanagement.R;
import com.example.agriculturalmanagement.model.entities.PhysicalAddress;

public class AddressEditorFragment extends Fragment {

    private EditText zipCode;
    private EditText country;
    private EditText municipality;
    private EditText city;
    private EditText district;
    private EditText street;
    private EditText number;
    private EditText parcelNumber;
    private boolean validAddress;

    private PhysicalAddress address;

    private Button setAddress;

    private Toast errorMessage;

    public AddressEditorFragment() {
        
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        address = new PhysicalAddress();
        validAddress = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_address_editor, container, false);

        zipCode = rootView.findViewById(R.id.zipCode);
        country = rootView.findViewById(R.id.country);
        municipality = rootView.findViewById(R.id.municipality);
        city = rootView.findViewById(R.id.city);
        district = rootView.findViewById(R.id.district);
        street = rootView.findViewById(R.id.street);
        number = rootView.findViewById(R.id.number);
        parcelNumber = rootView.findViewById(R.id.parcelNumber);

        // by default the values are empty

        zipCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {

                // todo test it
                try {

                    address.setZipCode(editable.toString().trim());
                    zipCode.setTextColor(Color.BLACK);
                    validAddress = true;
                }
                catch (Exception e){

                    zipCode.setTextColor(Color.RED);
                    validAddress = false;
                    errorMessage = Toast.makeText(getContext(),
                            "Could not set zip code, try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        country.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {

                // todo test it
                try {

                    address.setCountry(editable.toString().trim());
                    country.setTextColor(Color.BLACK);
                    validAddress = true;
                }
                catch (Exception e) {

                    country.setTextColor(Color.RED);
                    validAddress = false;
                    errorMessage = Toast.makeText(getContext(),
                            "Could not set country, try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        municipality.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {

                // todo test it
                try {

                    address.setMunicipality(editable.toString().trim());
                    municipality.setTextColor(Color.BLACK);
                    validAddress = true;
                }
                catch(Exception e) {

                    municipality.setTextColor(Color.RED);
                    validAddress = false;
                    errorMessage = Toast.makeText(getContext(),
                            "Could not set municipality, try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        city.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {

                // todo test it
                try {

                    address.setCity(editable.toString().trim());
                    city.setTextColor(Color.BLACK);
                    validAddress = true;
                }
                catch (Exception e) {

                    city.setTextColor(Color.RED);
                    validAddress = false;
                    errorMessage = Toast.makeText(getContext(),
                            "Could not set city, try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        district.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {

                // todo test it
                try {

                    address.setDistrict(editable.toString().trim());
                    district.setTextColor(Color.BLACK);
                    validAddress = true;
                }
                catch (Exception e) {

                    district.setTextColor(Color.RED);
                    validAddress = false;
                    errorMessage = Toast.makeText(getContext(),
                            "Could not set district, try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });
        
        street.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {

                // todo test it
                try {

                    address.setStreet(editable.toString().trim());
                    street.setTextColor(Color.BLACK);
                    validAddress = true;
                }
                catch (Exception e) {

                    street.setTextColor(Color.RED);
                    validAddress = false;
                    errorMessage = Toast.makeText(getContext(),
                            "Could not set street, try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });
        
        number.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {

                // todo test it
                try {
                    
                    address.setNumber(editable.toString().trim());
                    number.setTextColor(Color.BLACK);
                    validAddress = true;
                }
                catch (Exception e) {
                    
                    number.setTextColor(Color.RED);
                    validAddress = false;
                    errorMessage = Toast.makeText(getContext(),
                            "Could not set number, try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });
        
        parcelNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {

                // todo test it
                try {

                    address.setParcelNumber(editable.toString().trim());
                    parcelNumber.setTextColor(Color.BLACK);
                    validAddress = true;
                }
                catch (Exception e) {

                    parcelNumber.setTextColor(Color.RED);
                    validAddress = false;
                    errorMessage = Toast.makeText(getContext(),
                            "Could not set parcel number, try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        // setting default values
        try {

            address.setZipCode(zipCode.getText().toString());
            address.setCountry(country.getText().toString());
            address.setMunicipality(municipality.getText().toString());
            address.setCity(city.getText().toString());
            address.setDistrict(district.getText().toString());
            address.setStreet(street.getText().toString());
            address.setNumber(number.getText().toString());
            address.setParcelNumber(parcelNumber.getText().toString());
        }
        catch(Exception e) {

            errorMessage = Toast.makeText(getContext(),
                    "Could not set default values, try again.", Toast.LENGTH_LONG);
            errorMessage.show();
        }

        setAddress = rootView.findViewById(R.id.setAddress);
        setAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try {

                    Bundle bundle = new Bundle();
                    bundle.putString("address", address.toString());
                    getParentFragmentManager().setFragmentResult("addressEditorResult", bundle);
                    Navigation.findNavController(view).popBackStack();
                }
                catch (Exception e) {

                    zipCode.setTextColor(Color.RED);
                    country.setTextColor(Color.RED);
                    municipality.setTextColor(Color.RED);
                    city.setTextColor(Color.RED);
                    district.setTextColor(Color.RED);
                    street.setTextColor(Color.RED);
                    number.setTextColor(Color.RED);
                    parcelNumber.setTextColor(Color.RED);

                    errorMessage = Toast.makeText(getContext(),
                            "Could not set address, try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                    System.out.println(e.getMessage());
                }
            }
        });

        return rootView;
    }
}