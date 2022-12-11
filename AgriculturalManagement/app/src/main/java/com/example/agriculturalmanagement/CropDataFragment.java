package com.example.agriculturalmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.agriculturalmanagement.model.AppViewModel;

public class CropDataFragment extends Fragment {
    public CropDataFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crop_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        var args = CropDataFragmentArgs.fromBundle(getArguments());
        var activity = (AppCompatActivity) requireActivity();
        var viewModel = new ViewModelProvider(activity).get(AppViewModel.class);

        viewModel.getCropById(args.getCropId()).observe(getViewLifecycleOwner(), item -> {
            if (item == null)
                return;

            activity.getSupportActionBar().setTitle(item.getName());
        });
    }
}
