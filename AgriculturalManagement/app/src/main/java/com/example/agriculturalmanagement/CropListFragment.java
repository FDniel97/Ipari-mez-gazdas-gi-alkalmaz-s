package com.example.agriculturalmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriculturalmanagement.model.AppViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CropListFragment extends Fragment {
    public CropListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crop_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fabNewCrop = view.findViewById(R.id.crop_list_new_crop);
        fabNewCrop.setOnClickListener(v -> {
            // TODO
        });

        RecyclerView recyclerView = view.findViewById(R.id.crop_list_recycler_view);
        final var adapter = new CropListAdapter(item -> {
            // TODO
        });

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);

        var viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.getAllCrops().observe(getViewLifecycleOwner(), newList -> {
            adapter.submitList(newList);
        });
    }
}
