package com.example.agriculturalmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.agriculturalmanagement.model.AppViewModel;
import com.example.agriculturalmanagement.util.ResultReceiver;

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

        var navController = Navigation.findNavController(view);
        var activity = requireActivity();

        var viewModel = new ViewModelProvider(activity).get(AppViewModel.class);
        viewModel.getFieldById(args.getFieldId()).observe(getViewLifecycleOwner(), field -> {
            if (field == null)
                return;

            Toast.makeText(requireContext(), "name: " + field.getName(), Toast.LENGTH_SHORT).show();
        });

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
    }
}
