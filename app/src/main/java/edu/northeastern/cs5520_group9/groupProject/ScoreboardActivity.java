package edu.northeastern.cs5520_group9.groupProject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.MessageFormat;
import java.util.ArrayList;

import edu.northeastern.cs5520_group9.R;
import edu.northeastern.cs5520_group9.groupProject.model.Score;
import edu.northeastern.cs5520_group9.groupProject.model.ScoreAdapter;

public class ScoreboardActivity extends AppCompatActivity {
    private String level;
    private static String USERNAME;
    private final ArrayList<Score> scoresList = new ArrayList<>();
    private ScoreAdapter scoresAdapter;
    private TextView bestScoreView, playedNumbersView;
    private Integer numbers;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        setUI(); // Set UI visibility
        getUsername(); // get username
        level = "Easy"; // Default easy level

        // Set views, button and RecyclerView
        playedNumbersView = findViewById(R.id.playedNums);
        bestScoreView = findViewById(R.id.bestScore);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> startActivity(
                new Intent(ScoreboardActivity.this, GameSetting.class)
        ));
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        scoresAdapter = new ScoreAdapter(scoresList);
        recyclerView.setAdapter(scoresAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        scoreLevel(); // Get score level
        getPlayedNumbers(); // Get number of games played
        bestScore(); // Get best score
        scoreRanking(); // Get score ranking
        getToken(); // get user's device token
    }

    // Set the UI visibility
    private void setUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Get User Name
    private void getUsername() {
        USERNAME = getIntent().getExtras().getString("USERNAME");
    }

    // Get the score level
    private void scoreLevel() {
        MaterialButtonToggleGroup scoreButtonToggleGroup = findViewById(R.id.scoreButtonToggleGroup);
        scoreButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.easy) { // Easy Level
                    level = "Easy";
                } else if (checkedId == R.id.medium) { // Medium Level
                    level = "Medium";
                } else if (checkedId == R.id.hard) { // Hard Level
                    level = "Hard";
                }
                bestScore(); // Get best score
                scoreRanking(); // Get score ranking
            }
        });
    }

    // Get Best Score
    private void bestScore() {
        database.child("Users").child(USERNAME)
                .child("personalBestScore" + level)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(ScoreboardActivity.this,
                                    "You didn't played a game.", Toast.LENGTH_LONG).show();
                        } else {
                            Integer bestScore = snapshot.getValue(Integer.class);
                            bestScoreView.setText(MessageFormat.format(
                                    "Your personal best score: {0}", bestScore));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });
    }

    // Get Score Ranking
    private void scoreRanking() {
        database.child("Scores").child(level.toLowerCase()).orderByChild("score")
                .limitToLast(50).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        scoresList.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            Score score = dataSnapshot.getValue(Score.class);
                            scoresList.add(0, score);
                        }
                        // update adapter
                        scoresAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });
    }

    // Get user's device token
    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        database.child("Users").child(USERNAME)
                                .child("token").setValue(token);
                    } else if (!task.isSuccessful()) {
                        Log.w("TAG", "Get FCM token failed", task.getException());
                    }
                });
    }

    // Get numbers of game played
    private void getPlayedNumbers() {
        database.child("Users").child(USERNAME).child("numOfGamesPlayed")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        numbers = snapshot.getValue(Integer.class);
                        playedNumbersView.setText(MessageFormat.format(
                                "{0}, you have played {1} rounds", USERNAME, numbers));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });
    }
}
