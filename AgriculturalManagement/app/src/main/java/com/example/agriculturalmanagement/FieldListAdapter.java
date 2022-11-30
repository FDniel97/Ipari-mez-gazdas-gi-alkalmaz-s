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

class FieldListItemViewHolder extends RecyclerView.ViewHolder {
    private TextView name;

    private FieldListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.field_name);
    }

    static FieldListItemViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.field_list_item, parent, false);
        return new FieldListItemViewHolder(view);
    }

    void bind(@NonNull Field item) {
        name.setText(item.getName());
    }
}

public class FieldListAdapter extends ListAdapter<Field, FieldListItemViewHolder> {
    public FieldListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public FieldListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return FieldListItemViewHolder.create(parent);
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
