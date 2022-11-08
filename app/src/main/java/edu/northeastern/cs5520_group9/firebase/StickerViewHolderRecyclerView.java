package edu.northeastern.cs5520_group9.firebase;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

import edu.northeastern.cs5520_group9.R;

/**
 * Recycler view view holder class implementation
 */

public class StickerViewHolderRecyclerView extends RecyclerView.ViewHolder {
    private final ImageView receivedView;
    private final TextView fromUser;
    private final TextView sendTime;

    public StickerViewHolderRecyclerView(@NonNull View itemView) {
        super(itemView);

        this.receivedView = itemView.findViewById(R.id.receivedView);
        this.fromUser = itemView.findViewById(R.id.fromUser);
        this.sendTime = itemView.findViewById(R.id.sendTime);
    }


    public void bindThisData(Sticker sticker) {
        int imageResource = getImageResourceById(sticker.getImageId());
        if (imageResource != -1) {
            receivedView.setImageResource(imageResource);
        }
        fromUser.setText(sticker.getFromUser());
        sendTime.setText(new Date(sticker.getSendTimeEpochSecond()).toString());
    }

    // to get sticker by id
    private int getImageResourceById(int id) {
        if (id == 1) {
            return R.drawable.panda;
        } else if (id == 2) {
            return R.drawable.polar_bear;
        }
        return -1;
    }
}