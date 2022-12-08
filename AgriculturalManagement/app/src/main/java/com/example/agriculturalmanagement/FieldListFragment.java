package com.example.agriculturalmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriculturalmanagement.model.AppViewModel;
import com.example.agriculturalmanagement.model.entities.Field;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.stream.Collectors;

public class FieldListFragment extends Fragment {
    private LiveData<List<Field>> allFields;

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

        final var navController = Navigation.findNavController(view);

        FloatingActionButton fabNewField = view.findViewById(R.id.field_list_new_field);
        fabNewField.setOnClickListener(v -> {
            navController.navigate(R.id.action_field_list_dest_to_new_field_dest);
        });

        RecyclerView recyclerView = view.findViewById(R.id.field_list_recycler_view);
        final var adapter = new FieldListAdapter(field -> {
            navController.navigate(FieldListFragmentDirections.actionFieldListDestToFieldDataDest(field.getId()));
        });

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);

        var viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        allFields = viewModel.getAllFields();
        allFields.observe(getViewLifecycleOwner(), newList -> {
            // the filtering based on the search text is lost, but it is a sacrifice I am willing to make...
            adapter.submitList(newList);
        });

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.field_list_menu, menu);

                // I don't think this is the recommended way but hey it appears to work...
                var searchView = (SearchView) menu.findItem(R.id.menu_option_field_search).getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String searchText) {
                        var items = allFields.getValue();
                        if (items == null)
                            return true;

                        if (searchText.isEmpty()) {
                            adapter.submitList(items);
                            return true;
                        }

                        // yes, it's inefficient, why do you ask?
                        var results = items
                                .stream()
                                .filter(f -> f.getName().contains(searchText))
                                .collect(Collectors.toList());

                        adapter.submitList(results);
                        return true;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner());
    }
}
