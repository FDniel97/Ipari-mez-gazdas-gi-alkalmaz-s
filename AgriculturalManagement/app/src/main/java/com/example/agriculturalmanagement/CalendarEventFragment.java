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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.agriculturalmanagement.databinding.FragmentCalendarEventBinding;
import com.example.agriculturalmanagement.databinding.FragmentCropDataBinding;
import com.example.agriculturalmanagement.model.AppViewModel;
import com.example.agriculturalmanagement.model.entities.Field;
import com.example.agriculturalmanagement.util.ResultReceiver;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;

public class CalendarEventFragment extends Fragment {
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> datetime = new MutableLiveData<>();
    public MutableLiveData<String> fieldName = new MutableLiveData<>();
    public MutableLiveData<String> desc = new MutableLiveData<>();
    private LiveData<Field> field;

    public CalendarEventFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var binding = FragmentCalendarEventBinding.inflate(inflater, container, false);
        binding.setViewModel(this);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        var args = CalendarEventFragmentArgs.fromBundle(getArguments());
        var eventId = args.getEventId();

        var navController = Navigation.findNavController(view);
        var activity = (AppCompatActivity) requireActivity();
        var viewModel = new ViewModelProvider(activity).get(AppViewModel.class);

        final Observer<Field> fieldObserver = item -> {
            fieldName.setValue(item.getName());
        };

        viewModel.getCalendarEventById(eventId).observe(getViewLifecycleOwner(), item -> {
            if (item == null)
                return;

            activity.getSupportActionBar().setTitle(item.getName());
            name.setValue(item.getName());
            datetime.setValue(DATE_FORMAT.format(item.getTimestamp()));
            desc.setValue(item.getDescription());

            if (field != null)
                field.removeObserver(fieldObserver);

            field = viewModel.getFieldById(item.getFieldId());
            field.observe(getViewLifecycleOwner(), fieldObserver);
        });

        activity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.calendar_event_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_option_edit_event) {
                    navController.navigate(CalendarEventFragmentDirections.actionCalendarEventDestToNewCalendarEventDest().setEventId(eventId));
                } else if (menuItem.getItemId() == R.id.menu_option_delete_event) {
                    viewModel.deleteCalendarEventById(eventId, new ResultReceiver<>() {
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

        {
            MaterialCardView c = view.findViewById(R.id.calendar_event_field_name);
            c.setOnClickListener(v -> {
                if (field != null && field.getValue() != null)
                    navController.navigate(CalendarEventFragmentDirections.actionCalendarEventDestToFieldDataDest(field.getValue().getId()));
            });
        }
    }
}
