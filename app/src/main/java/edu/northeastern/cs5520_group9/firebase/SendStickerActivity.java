package edu.northeastern.cs5520_group9.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.HashSet;
import java.util.Set;

import edu.northeastern.cs5520_group9.R;

public class SendStickerActivity extends AppCompatActivity {
    private Button viewHistory;
    private Set<Integer> selectedImageIds = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);
        
        viewHistory = findViewById(R.id.sendStrick_checkhistory);
        viewHistory.setOnClickListener(view -> CheckHistoryBTPress());
    }

    public void clickImage(View view) {
        if (selectedImageIds.contains(view.getId())) {
            // unselect
            ((ImageView) view).setColorFilter(null);
            selectedImageIds.remove(view.getId());
        } else {
            // select
            ((ImageView) view).setColorFilter(ContextCompat.getColor(this.getApplicationContext(), R.color.purple_200), android.graphics.PorterDuff.Mode.MULTIPLY);
            selectedImageIds.add(view.getId());
        }
    }

    private void CheckHistoryBTPress() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
}