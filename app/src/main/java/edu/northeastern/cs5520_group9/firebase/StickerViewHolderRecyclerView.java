package edu.northeastern.cs5520_group9.firebase;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.northeastern.cs5520_group9.R;

/**
 * Recycler view view holder class implementation
 */

public class StickerViewHolderRecyclerView extends RecyclerView.ViewHolder {
    final ImageView receivedStickerView;
    final TextView fromUserView;
    final TextView sendTimeView;

    public StickerViewHolderRecyclerView(@NonNull View itemView) {
        super(itemView);

        this.receivedStickerView = itemView.findViewById(R.id.receivedStickerView);
        this.fromUserView = itemView.findViewById(R.id.fromUserView);
        this.sendTimeView = itemView.findViewById(R.id.sendTimeView);
    }


    public void bindThisData(Sticker sticker) {
        int imageResource = getImageResourceById(sticker.getImageId());
        System.out.println(imageResource);
        if (imageResource != -1) {
            receivedStickerView.setImageResource(imageResource);
        }
        fromUserView.setText(sticker.getFromUser());
        DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm:ss zzz");
        sendTimeView.setText(df.format(new Date(sticker.getSendTimeEpochSecond())));
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