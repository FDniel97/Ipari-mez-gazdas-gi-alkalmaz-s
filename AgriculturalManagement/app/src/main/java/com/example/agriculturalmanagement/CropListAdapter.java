package com.example.agriculturalmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriculturalmanagement.model.entities.Crop;

import java.util.function.Consumer;

class CropListItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private Crop item;

    private CropListItemViewHolder(@NonNull View itemView, @NonNull Consumer<Crop> onClick) {
        super(itemView);
        name = itemView.findViewById(R.id.crop_name);

        itemView.setOnClickListener(v -> {
            if (item != null)
                onClick.accept(item);
        });
    }

    static CropListItemViewHolder create(ViewGroup parent, Consumer<Crop> clickListener) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crop_list_item, parent, false);
        return new CropListItemViewHolder(view, clickListener);
    }

    void bind(@NonNull Crop item) {
        this.item = item;
        name.setText(item.getName());
    }
}

public class CropListAdapter extends ListAdapter<Crop, CropListItemViewHolder> {
    private final Consumer<Crop> clickListener;

    public CropListAdapter(Consumer<Crop> clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CropListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return CropListItemViewHolder.create(parent, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CropListItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    private static final DiffUtil.ItemCallback<Crop> DIFF_CALLBACK = new DiffUtil.ItemCallback<Crop>() {
        @Override
        public boolean areItemsTheSame(@NonNull Crop oldItem, @NonNull Crop newItem) {
            return oldItem.getId() > 0 && oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Crop oldItem, @NonNull Crop newItem) {
            return false; // FIXME
        }
    };
}
