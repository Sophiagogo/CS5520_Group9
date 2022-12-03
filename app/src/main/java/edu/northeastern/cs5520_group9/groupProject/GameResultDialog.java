package edu.northeastern.cs5520_group9.groupProject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import edu.northeastern.cs5520_group9.R;


public class GameResultDialog extends AppCompatDialogFragment {
    private LinearLayout gameResultWaiting, gameResultDone;
    private TextView scoreMessageView, tvPersonalBestNotification;
    private Button btnBackToGameSetting;
    private int gameScore, opponentScore;
    private boolean personalBestFlag, gameMode, onlineGameFinished;
    private String opponentName, opponentNumber, matchID;

    //constructor
    public GameResultDialog(boolean gameMode, int gameScore, boolean personalBestFlag) {
        this.gameMode = gameMode;
        this.gameScore = gameScore;
        this.personalBestFlag = personalBestFlag;
    }

    public GameResultDialog(int gameScore, boolean personalBestFlag, boolean gameMode, boolean onlineGameFinished, String opponentName, String opponentNumber, String matchID) {
        this.gameScore = gameScore;
        this.personalBestFlag = personalBestFlag;
        this.gameMode = gameMode;
        this.opponentName = opponentName;
        this.onlineGameFinished = onlineGameFinished;
        this.matchID = matchID;
        this.opponentNumber = opponentNumber;
    }

    public GameResultDialog(int gameScore, boolean personalBestFlag, boolean gameMode, boolean onlineGameFinished, String opponentName, int opponentScore) {
        this.gameScore = gameScore;
        this.personalBestFlag = personalBestFlag;
        this.gameMode = gameMode;
        this.opponentName = opponentName;
        this.onlineGameFinished = onlineGameFinished;
        this.opponentScore = opponentScore;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.game_result_dialog, null);

        scoreMessageView = view.findViewById(R.id.scoreMessage);
        gameResultWaiting = view.findViewById(R.id.gameResultWaiting);
        gameResultDone = view.findViewById(R.id.gameResultDone);

        if (gameMode) {
            showSoloGameResult();
        } else {
            if (!onlineGameFinished) {
                onlineGameWaiting();
            } else {
                showOnlineGameResult();
            }
        }

        // set personal best notification
        tvPersonalBestNotification = view.findViewById(R.id.tvPersonalBestNotification);

        if (personalBestFlag) {
            tvPersonalBestNotification.setVisibility(View.VISIBLE);
        }

        // set back to game setting button
        btnBackToGameSetting = view.findViewById(R.id.btnBackToGameSetting);
        btnBackToGameSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameSettingActivity();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    private void onlineGameWaiting() {
        gameResultWaiting.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child("Matches")
                .child(matchID)
                .child(opponentNumber)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("gameOver").getValue(Boolean.TYPE)) {
                            opponentScore = snapshot.child("score").getValue(Integer.class);
                            showOnlineGameResult();
                            // destroy match
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("Matches")
//                                    .child(matchID).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void showOnlineGameResult() {
        String scoreMessage = "";
        if (gameScore > opponentScore) {
            scoreMessage = "You win!\n Your score: " + gameScore + "\n" + opponentName + "'s score: " + opponentScore;
        } else if (gameScore == opponentScore) {
            scoreMessage = "Tie!\n Your score: " + gameScore + "\n" + opponentName + "'s score: " + opponentScore;
        } else {
            scoreMessage = "You lose!\n Your score: " + gameScore + "\n" + opponentName + "'s score: " + opponentScore;
        }
        scoreMessageView.setText(scoreMessage);
        gameResultWaiting.setVisibility(View.INVISIBLE);
        gameResultDone.setVisibility(View.VISIBLE);
    }

    private void showSoloGameResult() {
        String scoreMessage = "";

        if (gameScore >= 100) {
            scoreMessage = "Bravo! Your Score is ";
        } else if (gameScore >= 90) {
            scoreMessage = "Good job! Your Score is ";
        } else {
            scoreMessage = "Nice! Your Score is ";
        }
        scoreMessageView.setText(scoreMessage + this.gameScore + ".");
        gameResultDone.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();        Dialog dialog = getDialog();
        // set transparent window
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // disable closing dialog on outside touch
        dialog.setCanceledOnTouchOutside(false);
    }

    private void openGameSettingActivity() {
        Intent intent = new Intent(getContext(), GameSetting.class);
        startActivity(intent);
    }



}