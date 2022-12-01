package com.example.agriculturalmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriculturalmanagement.model.entities.Field;

import java.util.function.Consumer;

class FieldListItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private Field field;

    private FieldListItemViewHolder(@NonNull View itemView, @NonNull Consumer<Field> clickListener) {
        super(itemView);
        name = itemView.findViewById(R.id.field_name);

        itemView.setOnClickListener(v -> {
            if (field != null)
                clickListener.accept(field);
        });
    }

    static FieldListItemViewHolder create(ViewGroup parent, Consumer<Field> clickListener) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.field_list_item, parent, false);
        return new FieldListItemViewHolder(view, clickListener);
    }

    void bind(@NonNull Field item) {
        name.setText(item.getName());
        field = item;
    }
}

public class FieldListAdapter extends ListAdapter<Field, FieldListItemViewHolder> {
    private final Consumer<Field> clickListener;

    public FieldListAdapter(Consumer<Field> clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FieldListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return FieldListItemViewHolder.create(parent, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldListItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    private static final DiffUtil.ItemCallback<Field> DIFF_CALLBACK = new DiffUtil.ItemCallback<Field>() {
        @Override
        public boolean areItemsTheSame(@NonNull Field oldItem, @NonNull Field newItem) {
            return oldItem.getId() > 0 && oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Field oldItem, @NonNull Field newItem) {
            return false; // FIXME
        }
    };
}
