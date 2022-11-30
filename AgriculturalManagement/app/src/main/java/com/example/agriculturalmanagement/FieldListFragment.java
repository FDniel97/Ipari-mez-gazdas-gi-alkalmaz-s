package com.example.agriculturalmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriculturalmanagement.model.AppViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FieldListFragment extends Fragment {
    public FieldListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_field_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fabNewField = view.findViewById(R.id.field_list_new_field);
        fabNewField.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_field_list_dest_to_new_field_dest));

        RecyclerView recyclerView = view.findViewById(R.id.field_list_recycler_view);
        final var adapter = new FieldListAdapter();

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);

        var viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.getAllFields().observe(getViewLifecycleOwner(), newList -> {
            adapter.submitList(newList);
        });
    }
}
