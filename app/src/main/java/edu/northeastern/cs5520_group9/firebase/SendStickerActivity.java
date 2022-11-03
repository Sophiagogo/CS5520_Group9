package edu.northeastern.cs5520_group9.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.cs5520_group9.R;

public class SendStickerActivity extends AppCompatActivity {
    private Button viewHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);
        
        viewHistory = findViewById(R.id.sendStrick_checkhistory);
        viewHistory.setOnClickListener(view -> CheckHistoryBTPress());
        
        
    }

    private void CheckHistoryBTPress() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
}