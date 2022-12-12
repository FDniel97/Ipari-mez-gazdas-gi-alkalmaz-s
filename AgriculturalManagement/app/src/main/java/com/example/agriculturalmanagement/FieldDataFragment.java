package com.example.agriculturalmanagement;

import android.os.Bundle;
import android.renderscript.Script;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agriculturalmanagement.databinding.FragmentFieldDataBinding;
import com.example.agriculturalmanagement.databinding.FragmentOverviewBinding;
import com.example.agriculturalmanagement.model.AppViewModel;
import com.example.agriculturalmanagement.model.entities.Field;
import com.example.agriculturalmanagement.model.entities.FieldWithCropAndEvents;
import com.example.agriculturalmanagement.util.ResultReceiver;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FieldDataFragment extends Fragment {
    public MutableLiveData<Integer> fieldCount = new MutableLiveData<>(0);

    public FieldDataFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_field_data, container, false);
        var binding = FragmentFieldDataBinding.inflate(inflater, container, false);
        binding.setViewModel(this);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getBackground().setAlpha(180);
        var args = FieldDataFragmentArgs.fromBundle(getArguments());

        var navController = Navigation.findNavController(view);

        var activity = (AppCompatActivity) requireActivity();

        var viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        {
            MaterialCardView c = view.findViewById(R.id.field_weather_card);
            c.setOnClickListener(v -> {
                // https://stackoverflow.com/questions/71565073/why-does-navigation-not-work-in-the-navigation-drawer-activity-template-with-ver
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.weather_dest, false, false)
                        .setRestoreState(true)
                        .build();
                navController.navigate(FieldDataFragmentDirections.actionFieldDataDestToWeatherFragmentDest(), navOptions);

            });
        }

        activity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.field_data_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_option_delete_field) {
                    viewModel.deleteFieldById(args.getFieldId(), new ResultReceiver<>() {
                        @Override
                        public void onSuccess(Void value) {
                            Toast.makeText(requireContext(), R.string.success, Toast.LENGTH_SHORT).show();
                            navController.popBackStack(); // FIXME: needs a more robust way
                        }

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        }
                    });

                    return true;
                }

                return false;
            }
        }, getViewLifecycleOwner());

        var adapter = new CalendarEventListAdapter(item -> {
            navController.navigate(FieldDataFragmentDirections.actionFieldDataDestToCalendarEventDest(item.getId()));
        });

        RecyclerView recyclerView = view.findViewById(R.id.field_event_recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);

        viewModel.getFieldWithCropAndEventsById(args.getFieldId()).observe(getViewLifecycleOwner(), item -> {
            if (item == null)
                return;

            activity.getSupportActionBar().setTitle(item.field.getName());
            adapter.submitList(item.events);

            setUI(item);
            updateTemperature(item.field);
        });
    }

    private void setUI(FieldWithCropAndEvents data)
    {
        TextView name = (TextView) getView().findViewById(R.id.field_name);
        TextView longitude = (TextView) getView().findViewById(R.id.longitude);
        TextView latitude = (TextView) getView().findViewById(R.id.latitude);
        TextView cropType = (TextView) getView().findViewById(R.id.cropType);

        name.setText("Name: " + data.field.getName());
        longitude.setText("Longitude= " + data.field.getLocationLongitude());
        latitude.setText("Latitude= " + data.field.getLocationLatitude());
        try {
            cropType.setText("Crop type: " + data.crop.getName());
        }
        catch (Exception e)
        {
            cropType.setText("None");
        }
    }

    private void updateTemperature(Field field) {
        double latitude = field.getLocationLatitude();
        double longitude = field.getLocationLongitude();
        String url = String.format("https://api.open-meteo.com/v1/forecast?latitude=" +
                        "%f" +
                        "&longitude=" +
                        "%f" +
                        "&daily=temperature_2m_max,temperature_2m_min,precipitation_sum,windspeed_10m_max,shortwave_radiation_sum&current_weather=true&timezone=auto",
                latitude, longitude);

        RequestQueue queue = Volley.newRequestQueue(getContext());

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
                            double temp = responseMap.get("current_weather").get("temperature").asDouble();
                            fieldCount.setValue((int)temp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
        });
        //////////////////////////////////////////////////////////////////////////////////

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
