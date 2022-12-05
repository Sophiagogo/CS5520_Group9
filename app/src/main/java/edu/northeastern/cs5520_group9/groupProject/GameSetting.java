package edu.northeastern.cs5520_group9.groupProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import edu.northeastern.cs5520_group9.R;
import edu.northeastern.cs5520_group9.groupProject.model.User;


public class GameSetting extends AppCompatActivity {
    MaterialButtonToggleGroup tgBtnGrpOperation, tgBtnGrpLevel, tgBtnGrpMode;
    Button btnLogout, btnSettingDone, btnScoreBoard;
    String gameOperation, gameLevel;
    boolean gameMode;
    FirebaseAuth auth;
    String usernameString = "";
    FirebaseUser currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setting);

        hideSystemUI();
        // set onClickListener for toggle button groups
        setGameOperation();
        setGameLevel();
        setGameMode();

        currUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        readUsername(dbRef, new DataListener() {
            @Override
            public void onSuccess(String dataSnapShotValue) {
                usernameString = dataSnapShotValue;
            }
        });

        btnSettingDone = findViewById(R.id.btnSettingDone);
        btnSettingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure all buttons are checked
                if (tgBtnGrpLevel.getCheckedButtonId() == View.NO_ID
                        || tgBtnGrpOperation.getCheckedButtonId() == View.NO_ID
                        || tgBtnGrpMode.getCheckedButtonId() == View.NO_ID) {
                    Toast.makeText(GameSetting.this, "All settings required!", Toast.LENGTH_SHORT).show();
                } else {
                    if (gameMode) {
                        openGameActivity();
                    } else {
                        openMatchingActivity();
                    }
                }
            }
        });

        btnScoreBoard = findViewById(R.id.btnScoreBoard);
        btnScoreBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScoreBoard();
            }
        });

        btnLogout = findViewById(R.id.btnSettingLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    private void readUsername(DatabaseReference ref, final DataListener listener) {
        ref.child("Users").orderByChild("id").equalTo(currUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    usernameString = user.getUsername();
                    listener.onSuccess(usernameString);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void setGameOperation() {
        tgBtnGrpOperation = findViewById(R.id.toggleBtnGrpOperationSelection);
        tgBtnGrpOperation.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    switch (checkedId) {
                        case R.id.btnSettingGameAdd:
                            gameOperation = "+";
                            break;
                        case R.id.btnSettingGameSubtract:
                            gameOperation = "-";
                            break;
                    }
                }
            }
        });
    }

    private void setGameLevel() {
        tgBtnGrpLevel = findViewById(R.id.toggleBtnGrpLevelSelection);
        tgBtnGrpLevel.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    switch (checkedId) {
                        case R.id.btnSettingLevelEasy:
                            gameLevel = "easy";
                            break;
                        case R.id.btnSettingLevelMedium:
                            gameLevel = "medium";
                            break;
                        case R.id.btnSettingLevelHard:
                            gameLevel = "hard";
                            break;
                    }
                }
            }
        });
    }

    private void setGameMode() {
        tgBtnGrpMode = findViewById(R.id.toggleBtnGrpModeSelection);
        tgBtnGrpMode.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    switch (checkedId) {
                        case R.id.btnSettingModeSolo:
                            gameMode = true;
                            break;
                        case R.id.btnSettingModeComp:
                            gameMode = false;
                            break;
                    }
                }
            }
        });
    }

    private void logOut() {
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        openStartActivity();
    }

    private void openStartActivity() {
        Intent intent = new Intent(this, GroupProjectActivity.class);
        startActivity(intent);
    }

    private void openGameActivity() {
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra("GAME_OPERATION", gameOperation);
        intent.putExtra("GAME_LEVEL", gameLevel);
        intent.putExtra("GAME_MODE", gameMode);
        intent.putExtra("USERNAME", usernameString);
        startActivity(intent);
    }

    private void openMatchingActivity() {
        Intent intent = new Intent(this, MatchingActivity.class);
        intent.putExtra("GAME_OPERATION", gameOperation);
        intent.putExtra("GAME_LEVEL", gameLevel);
        intent.putExtra("GAME_MODE", gameMode);
        intent.putExtra("USERNAME", usernameString);
        startActivity(intent);
    }

    private void openScoreBoard() {
        Intent intent = new Intent(this, ScoreboardActivity.class);
        intent.putExtra("USERNAME", usernameString);
        startActivity(intent);
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void showLocation(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }
}