package edu.northeastern.cs5520_group9.team;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.cs5520_group9.R;

public class TeamNameActivity extends AppCompatActivity {

    private TextView teamName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_name);
        teamName = findViewById(R.id.team);
    }

    public TextView getTeamName() {
        return teamName;
    }

    public void setTeamName(TextView teamName) {
        this.teamName = teamName;
    }
}
