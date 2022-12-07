package edu.northeastern.cs5520_group9.groupProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.cs5520_group9.R;
import edu.northeastern.cs5520_group9.menu.MainActivity;
import edu.northeastern.cs5520_group9.team.TeamNameActivity;

public class MatchingActivity extends AppCompatActivity {
    DatabaseReference roomRef;
    DatabaseReference roomsRef;
    Button createButton;
    Button joinButton;
    TextView title;
    TextView availableRooms;
    FirebaseDatabase database;

    String GAME_LEVEL, GAME_OPERATION, USERNAME;
    boolean GAME_MODE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        createButton = findViewById(R.id.createBTN);
        joinButton = findViewById(R.id.joinBTN);
        title = findViewById(R.id.textView);
        availableRooms = findViewById(R.id.textView2);

        database = FirebaseDatabase.getInstance();

        getGameSettings();

        roomsRef = database.getReference("room");
        roomsRef.setValue("");

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createButton.setEnabled(false);
                roomRef = database.getReference("rooms player1");
                roomsRef = database.getReference("room");
                roomsRef.setValue("created");



//                addRoomEventListener();

                roomRef.setValue("player1");
                openGameActivity();
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference roomRef = database.getReference("rooms player2");

//                addRoomEventListener();
                roomRef.setValue("player2");
                openGameActivity();
            }
        });

        addRoomsEventListener();


    }

//    private void addRoomEventListener(){
//        roomRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                createButton.setEnabled(true);
//                openGameActivity();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                createButton.setEnabled(true);
//                Toast.makeText(MatchingActivity.this, "Error!",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void addRoomsEventListener(){
        roomsRef = database.getReference("room");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if  (snapshot.getValue(String.class).equals("created")) {
                    availableRooms.setText("Play1's Room is available");
                }

                else{
                    availableRooms.setText("No existing room. Waiting for creating room.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //nothing
            }
        });
    }

    private void openGameActivity() {
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra("GAME_OPERATION", GAME_OPERATION);
        intent.putExtra("GAME_LEVEL", GAME_LEVEL);
        intent.putExtra("GAME_MODE", true);
        intent.putExtra("USERNAME", USERNAME);
        startActivity(intent);
    }

    private void getGameSettings() {
        GAME_OPERATION = getIntent().getExtras().getString("GAME_OPERATION");
        GAME_LEVEL = getIntent().getExtras().getString("GAME_LEVEL");
        GAME_MODE = getIntent().getExtras().getBoolean("GAME_MODE");
        USERNAME = getIntent().getExtras().getString("USERNAME");
    }



}