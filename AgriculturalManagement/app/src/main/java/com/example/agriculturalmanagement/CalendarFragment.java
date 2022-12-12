package com.example.agriculturalmanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.agriculturalmanagement.model.AppViewModel;
import com.example.agriculturalmanagement.model.entities.CalendarEvent;

import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {
    private LiveData<List<CalendarEvent>> events;
    private CalendarEventListAdapter adapter;
    private AppViewModel viewModel;

    public CalendarFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        var navController = Navigation.findNavController(view);

        adapter = new CalendarEventListAdapter(item -> {
            navController.navigate(CalendarFragmentDirections.actionCalendarDestToCalendarEventDest(item.getId()));
        });

        RecyclerView recyclerView = view.findViewById(R.id.calendar_event_list_recycler_view);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);

        CalendarView calendarView = view.findViewById(R.id.calendar_view);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                updateShownEvents(year, month, dayOfMonth);
                Toast.makeText(requireContext(), String.format("%d-%d-%d", year, month, dayOfMonth), Toast.LENGTH_SHORT).show();
            }
        });

        {
            var d = new Date();
            updateShownEvents(d.getYear() + 1900, d.getMonth(), d.getDate());
            calendarView.setDate(d.getTime());
        }
    }

    private final Observer<List<CalendarEvent>> OBSERVER = newValue -> {
        adapter.submitList(newValue);
    };

    private void updateShownEvents(int year, int month, int dayOfMonth) {
        long from = new Date(year - 1900, month, dayOfMonth).getTime();
        long to = from + 24 * 3600 * 1000;

        if (events != null)
            events.removeObserver(OBSERVER);

        events = viewModel.getCalendarEventsInRange(from, to);
        events.observe(getViewLifecycleOwner(), OBSERVER);
    }
}
