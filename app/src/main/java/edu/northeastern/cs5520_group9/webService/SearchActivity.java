package edu.northeastern.cs5520_group9.webService;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.cs5520_group9.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);

        Button search = findViewById(R.id.search);
        search.setOnClickListener(view -> openSearch());
    }

    public void openSearch() {
        Intent intent = new Intent(this, getImageActivity.class);
        startActivity(intent);
    }
}
