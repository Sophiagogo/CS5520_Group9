package edu.northeastern.cs5520_group9.firebase;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.cs5520_group9.R;

/**
 * Recycler view adapter class implementation to display stickers history
 */

public class StickerAdapterRecyclerView extends RecyclerView.Adapter<StickerViewHolderRecyclerView> {
    // use list to store a list of stickers
    private final List<Sticker> stickerList;

    public StickerAdapterRecyclerView(List<Sticker> stickers) {
        this.stickerList = stickers;
    }

    @NonNull
    @Override
    public StickerViewHolderRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StickerViewHolderRecyclerView(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sticker_history_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolderRecyclerView viewHolder, int position) {
        viewHolder.bindThisData(stickerList.get(position));
    }

    // to get count of stickers
    @Override
    public int getItemCount() {
        if (stickerList == null) {
            return 0;
        } else {
            return stickerList.size();
        }
    }
}