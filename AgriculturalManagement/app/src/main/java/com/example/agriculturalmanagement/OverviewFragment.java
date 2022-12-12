package com.example.agriculturalmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.agriculturalmanagement.databinding.FragmentOverviewBinding;
import com.example.agriculturalmanagement.model.AppViewModel;
import com.google.android.material.card.MaterialCardView;

import java.util.Date;

public class OverviewFragment extends Fragment {
    public MutableLiveData<Integer> fieldCount = new MutableLiveData<>(0);
    public MutableLiveData<Integer> cropCount = new MutableLiveData<>(0);
    public MutableLiveData<Integer> eventCount = new MutableLiveData<>(0);

    public OverviewFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        var binding = FragmentOverviewBinding.inflate(inflater, container, false);
        binding.setViewModel(this);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        var navController = Navigation.findNavController(view);
        var viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        viewModel.getAllFields().observe(getViewLifecycleOwner(), newValue -> {
            fieldCount.setValue(newValue.size());
        });

        {
            MaterialCardView c = view.findViewById(R.id.field_count_card);
            c.setOnClickListener(v -> {
                // https://stackoverflow.com/questions/71565073/why-does-navigation-not-work-in-the-navigation-drawer-activity-template-with-ver
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.overview_dest, false, true)
                        .setRestoreState(true)
                        .build();
                navController.navigate(OverviewFragmentDirections.actionOverviewDestToFieldListDest(), navOptions);
            });
        }

        viewModel.getAllCrops().observe(getViewLifecycleOwner(), newValue -> {
            cropCount.setValue(newValue.size());
        });

        {
            MaterialCardView c = view.findViewById(R.id.crop_count_card);
            c.setOnClickListener(v -> {
                // https://stackoverflow.com/questions/71565073/why-does-navigation-not-work-in-the-navigation-drawer-activity-template-with-ver
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.overview_dest, false, true)
                        .setRestoreState(true)
                        .build();
                navController.navigate(OverviewFragmentDirections.actionOverviewDestToCropListDest(), navOptions);
            });
        }

        {
            var d = new Date();
            viewModel.getCalendarEventsInRange(d.getTime(), d.getTime() + 14 * 24 * 3600 * 1000).observe(getViewLifecycleOwner(), newValue -> {
                eventCount.setValue(newValue.size());
            });

            MaterialCardView c = view.findViewById(R.id.event_count_card);
            c.setOnClickListener(v -> {
                // https://stackoverflow.com/questions/71565073/why-does-navigation-not-work-in-the-navigation-drawer-activity-template-with-ver
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.overview_dest, false, true)
                        .setRestoreState(true)
                        .build();
                navController.navigate(OverviewFragmentDirections.actionOverviewDestToCalendarDest(), navOptions);
            });
        }
    }
}
