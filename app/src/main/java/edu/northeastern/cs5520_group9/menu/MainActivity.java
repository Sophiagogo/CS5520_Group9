package edu.northeastern.cs5520_group9.menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.cs5520_group9.R;
import edu.northeastern.cs5520_group9.firebase.FirebaseDBActivity;
import edu.northeastern.cs5520_group9.groupProject.GroupProjectActivity;
import edu.northeastern.cs5520_group9.team.TeamNameActivity;
import edu.northeastern.cs5520_group9.webService.WebServiceActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button teamName = findViewById(R.id.teamName);
        teamName.setOnClickListener(view -> openTeamNameActivity());

        Button webService = findViewById(R.id.webService);
        webService.setOnClickListener(view -> openWebServiceActivity());

        Button firebaseDB = findViewById(R.id.firebaseDB);
        firebaseDB.setOnClickListener(view -> openFirebaseDBActivity());

        Button groupProject = findViewById(R.id.groupProject);
        groupProject.setOnClickListener(view -> openGroupProjectActivity());

    }

    public void openTeamNameActivity() {
        Intent intent = new Intent(this, TeamNameActivity.class);
        startActivity(intent);
    }

    public void openWebServiceActivity() {
        Intent intent = new Intent(this, WebServiceActivity.class);
        startActivity(intent);
    }

    public void openFirebaseDBActivity() {
        Intent intent = new Intent(this, FirebaseDBActivity.class);
        startActivity(intent);
    }

    public void openGroupProjectActivity() {
        Intent intent = new Intent(this, GroupProjectActivity.class);
        startActivity(intent);
    }
}
