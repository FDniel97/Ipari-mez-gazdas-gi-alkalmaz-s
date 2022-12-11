package com.example.agriculturalmanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agriculturalmanagement.model.AppViewModel;
import com.example.agriculturalmanagement.model.entities.Crop;
import com.example.agriculturalmanagement.util.ResultReceiver;

public class NewCropDataFragment extends Fragment {
    private Crop editedEntity;

    public NewCropDataFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_crop_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        var cropId = NewCropDataFragmentArgs.fromBundle(getArguments()).getCropId();

        var activity = (AppCompatActivity) requireActivity();
        var viewModel = new ViewModelProvider(activity).get(AppViewModel.class);
        var navController = Navigation.findNavController(view);

        EditText nameField = view.findViewById(R.id.new_crop_data_crop_name);
        EditText descField = view.findViewById(R.id.new_crop_data_crop_desc);

        if (cropId > 0) {
            nameField.setEnabled(false);
            descField.setEnabled(false);

            viewModel.getCropById(cropId).observe(getViewLifecycleOwner(), item -> {
                if (editedEntity != null)
                    return;

                editedEntity = item;
                nameField.setText(item.getName());
                descField.setText(item.getDescription());
                nameField.setEnabled(true);
                descField.setEnabled(true);

                activity.getSupportActionBar().setTitle(item.getName());
            });
        }

        activity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.new_crop_data_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_option_save_crop) {
                    var name = nameField.getText().toString();
                    var desc = descField.getText().toString();

                    if (!name.isEmpty() && !desc.isEmpty()) {
                        var crop = new Crop(cropId, name, desc, -1, -1);

                        var resultReceiver = new ResultReceiver<Void>() {
                            @Override
                            public void onSuccess(Void value) {
                                Toast.makeText(requireContext(), R.string.success, Toast.LENGTH_SHORT).show();
                                navController.popBackStack();
                            }

                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        };

                        if (cropId > 0)
                            viewModel.updateCrop(crop, resultReceiver);
                        else
                            viewModel.insertCrop(crop, resultReceiver);
                    }

                    return true;
                }

                return false;
            }
        }, getViewLifecycleOwner());
    }
}
