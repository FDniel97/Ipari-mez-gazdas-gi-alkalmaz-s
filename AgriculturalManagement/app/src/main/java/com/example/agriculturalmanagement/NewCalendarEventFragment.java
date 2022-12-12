package com.example.agriculturalmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.agriculturalmanagement.model.AppViewModel;
import com.example.agriculturalmanagement.model.entities.CalendarEvent;
import com.example.agriculturalmanagement.model.entities.Crop;
import com.example.agriculturalmanagement.model.entities.Field;
import com.example.agriculturalmanagement.util.ResultReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

public class NewCalendarEventFragment extends Fragment {
    private CalendarEvent editedEntity;
    private int selectedFieldId;
    private EditText dateField;
    private LiveData<Field> field;

    public NewCalendarEventFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_calendar_event, container, false);
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        var args = NewCalendarEventFragmentArgs.fromBundle(getArguments());
        var activity = (AppCompatActivity) requireActivity();
        var viewModel = new ViewModelProvider(activity).get(AppViewModel.class);
        var navController = Navigation.findNavController(view);

        EditText nameField = view.findViewById(R.id.new_calendar_event_name);
        dateField = view.findViewById(R.id.new_calendar_event_datetime);
        EditText fieldField = view.findViewById(R.id.new_calendar_event_field);
        EditText descField = view.findViewById(R.id.new_calendar_event_description);

        fieldField.setOnClickListener(v -> {
            var builder = new AlertDialog.Builder(requireContext());
            var allFields = viewModel.getAllFields().getValue();

            builder.setItems(
                    allFields.stream().map(c -> (CharSequence) c.getName()).toArray(CharSequence[]::new),
                    (dialog, which) -> {
                        selectedFieldId = allFields.get(which).getId();
                        fieldField.setText(allFields.get(which).getName());
                    }
            );

            builder.show();
        });

        var eventId = args.getEventId();

        if (eventId > 0) {
            nameField.setEnabled(false);
            dateField.setEnabled(false);
            fieldField.setEnabled(false);
            descField.setEnabled(false);

            final Observer<Field> fieldObserver = item -> {
                nameField.setText(editedEntity.getName());
                dateField.setText(DATE_FORMAT.format(editedEntity.getTimestamp()));
                descField.setText(editedEntity.getDescription());

                fieldField.setText(item.getName());
                selectedFieldId = item.getId();

                nameField.setEnabled(true);
                dateField.setEnabled(true);
                fieldField.setEnabled(true);
                descField.setEnabled(true);

                dateField.setOnClickListener(v -> {
                    showDateTimePickers(editedEntity.getTimestamp());
                });
            };

            viewModel.getCalendarEventById(eventId).observe(getViewLifecycleOwner(), item -> {
                if (editedEntity != null)
                    return;

                editedEntity = item;

                if (field != null)
                    field.removeObserver(fieldObserver);

                field = viewModel.getFieldById(item.getFieldId());
                field.observe(getViewLifecycleOwner(), fieldObserver);
            });
        }
        else {
            Date d;
            if (args.getInitialDate() > 0) {
                d = new Date(args.getInitialDate());
            } else {
                d = new Date();
            }

            dateField.setText(DATE_FORMAT.format(d));
            dateField.setOnClickListener(v -> {
                showDateTimePickers(d);
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
                    var datetime = dateField.getText().toString();
                    var desc = descField.getText().toString();

                    if (!name.isEmpty() && !datetime.isEmpty() && selectedFieldId > 0 && !desc.isEmpty()) {
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

                        try {
                            var event = new CalendarEvent(eventId, selectedFieldId, name, DATE_FORMAT.parse(datetime), desc);
                            if (eventId > 0)
                                viewModel.updateCalendarEvent(event, resultReceiver);
                            else
                                viewModel.insertCalendarEvent(event, resultReceiver);
                        } catch (ParseException e) {
                            resultReceiver.onFailure(e);
                        }
                    }

                    return true;
                }

                return false;
            }
        }, getViewLifecycleOwner());
    }

    private void showDateTimePickers(Date d) {
        var dateDialog = new DatePickerDialog(requireContext(), (view1, year, month, dayOfMonth) -> {
            var timeDialog = new TimePickerDialog(requireContext(), (view2, hourOfDay, minute) -> {
                var selectedDate = new Date(year - 1900, month, dayOfMonth, hourOfDay, minute);
                dateField.setText(DATE_FORMAT.format(selectedDate));
            }, d.getHours(), d.getMinutes(), true);
            timeDialog.show();
        }, d.getYear() + 1900, d.getMonth(), d.getDate());
        dateDialog.show();
    }
}
