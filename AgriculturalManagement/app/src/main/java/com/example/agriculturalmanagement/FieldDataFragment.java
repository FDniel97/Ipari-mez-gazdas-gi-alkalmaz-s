package com.example.agriculturalmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.agriculturalmanagement.model.AppViewModel;

public class FieldDataFragment extends Fragment {
    public FieldDataFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_field_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        var args = FieldDataFragmentArgs.fromBundle(getArguments());
        Toast.makeText(requireContext(), "id: " + args.getFieldId(), Toast.LENGTH_SHORT).show();

        var viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.getFieldById(args.getFieldId()).observe(getViewLifecycleOwner(), field -> {
            Toast.makeText(requireContext(), "name: " + field.getName(), Toast.LENGTH_SHORT).show();
        });
    }
}
