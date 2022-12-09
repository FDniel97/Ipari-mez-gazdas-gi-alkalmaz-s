package com.example.agriculturalmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriculturalmanagement.model.entities.CalendarEvent;

import java.text.SimpleDateFormat;
import java.util.function.Consumer;

class CalendarEventListItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView datetime;
    private final TextView title;
    private CalendarEvent item;

    private CalendarEventListItemViewHolder(@NonNull View itemView, @NonNull Consumer<CalendarEvent> clickListener) {
        super(itemView);
        datetime = itemView.findViewById(R.id.event_datetime);
        title = itemView.findViewById(R.id.event_title);

        itemView.setOnClickListener(v -> {
            if (item != null)
                clickListener.accept(item);
        });
    }

    static CalendarEventListItemViewHolder create(ViewGroup parent, Consumer<CalendarEvent> clickListener) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_list_item, parent, false);
        return new CalendarEventListItemViewHolder(view, clickListener);
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    void bind(@NonNull CalendarEvent item) {
        this.item = item;
        datetime.setText(DATE_FORMAT.format(item.getTimestamp()));
        title.setText(item.getName());
    }
}

public class CalendarEventListAdapter extends ListAdapter<CalendarEvent, CalendarEventListItemViewHolder> {
    private final Consumer<CalendarEvent> clickListener;

    public CalendarEventListAdapter(Consumer<CalendarEvent> clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CalendarEventListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return CalendarEventListItemViewHolder.create(parent, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarEventListItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    private static final DiffUtil.ItemCallback<CalendarEvent> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull CalendarEvent oldItem, @NonNull CalendarEvent newItem) {
            return oldItem.getId() > 0 && oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CalendarEvent oldItem, @NonNull CalendarEvent newItem) {
            return false; // FIXME
        }
    };
}
