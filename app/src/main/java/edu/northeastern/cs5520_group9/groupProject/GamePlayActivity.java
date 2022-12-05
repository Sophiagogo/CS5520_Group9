package edu.northeastern.cs5520_group9.groupProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import edu.northeastern.cs5520_group9.R;
import edu.northeastern.cs5520_group9.groupProject.model.Player;
import edu.northeastern.cs5520_group9.groupProject.model.User;


public class GamePlayActivity extends AppCompatActivity {
    // multiple choice buttons
    private Button option1, option2, option3, option4, option5, homeButton;

    // images
    private ImageView m1, m2, m3, m4, m5, partyPopper;

    // textviews
    private TextView tvQuestion, tvScore, tvTime, tvOpponentName, tvOpponentTitle;
    private int seconds;

    // game settings
    static String GAME_LEVEL, GAME_OPERATION, USERNAME;
    static boolean GAME_MODE;
    private GameDesign game;
    private boolean personalBestFlag = false;
    static String MATCH_ID;

    // Firebase settings
    DatabaseReference rootDatabaseRef;
    User user;

    // Match settings
    int playerNumber;
    Player curPlayer;
    Player opponentPlayer;

    // sound effects
    private Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        hideSystemUI();

        // connect multiple choices and monsters to UI
        connectUIComponents();
        // install listeners to all multiple choices
        installListeners();
        // get game settings
        getGameSettings();
        // add sound effects
        sound = new Sound(this);

        // set root database reference
        rootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        if (GAME_MODE == true) {
            initGame();
        } else {
            getGameSettingOnline();
            onlineGame();
        }
    }


    private void getGameSettings() {
        GAME_OPERATION = getIntent().getExtras().getString("GAME_OPERATION");
        GAME_LEVEL = getIntent().getExtras().getString("GAME_LEVEL");
        GAME_MODE = getIntent().getExtras().getBoolean("GAME_MODE");
        USERNAME = getIntent().getExtras().getString("USERNAME");
    }

    private void getGameSettingOnline() {
        GAME_OPERATION = getIntent().getExtras().getString("GAME_OPERATION");
        GAME_LEVEL = getIntent().getExtras().getString("GAME_LEVEL");
        GAME_MODE = getIntent().getExtras().getBoolean("GAME_MODE");
        MATCH_ID = getIntent().getExtras().getString("MATCH_ID");
        USERNAME = getIntent().getExtras().getString("USERNAME");
    }

    private void storeGameScore() {
        rootDatabaseRef.child("Users").child(USERNAME).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);

                // update real-time number of games played
                updateNumOfGamesPlayed();

                // update user's personal best score if user gets a higher score
                updaterPersonalBestScore();

                // add this round of score to all scores
                addToScores();

                Toast.makeText(GamePlayActivity.this, "Score stored successfully.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateNumOfGamesPlayed() {
        user.numOfGamesPlayed++;
        rootDatabaseRef.child("Users")
                .child(USERNAME)
                .child("numOfGamesPlayed").setValue(user.numOfGamesPlayed);
    }

    private void addToScores() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("score", game.score);
        hashMap.put("username", USERNAME);
        rootDatabaseRef.child("Scores")
                .child(GAME_LEVEL)
                .push()
                .setValue(hashMap);
    }

    private void updaterPersonalBestScore() {
        switch (GAME_LEVEL) {
            case "easy":
                if (game.score > user.personalBestScoreEasy) {
                    rootDatabaseRef.child("Users")
                            .child(USERNAME)
                            .child("personalBestScoreEasy")
                            .setValue(game.score);
                    personalBestFlag = true;
                }
                break;
            case "medium":
                if (game.score > user.personalBestScoreMedium) {
                    rootDatabaseRef.child("Users")
                            .child(USERNAME)
                            .child("personalBestScoreMedium")
                            .setValue(game.score);
                    personalBestFlag = true;
                }
                break;
            case "hard":
                if (game.score > user.personalBestScoreHard) {
                    rootDatabaseRef.child("Users")
                            .child(USERNAME)
                            .child("personalBestScoreHard")
                            .setValue(game.score);
                    personalBestFlag = true;
                }
                break;

        };
    }

    private void onlineGame() {
        // create new game
        game = new GameDesign(GAME_OPERATION, GAME_LEVEL, GAME_MODE, 1, 0);
        game.questionQueue.clear();
        game.correctOptionQueue.clear();
        game.optionsQueue.clear();

        // find the match
        rootDatabaseRef.child("Matches").child(MATCH_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // retrieve questions and options
                for (int i = 1; i <= 10; i++) {
                    game.questionQueue.add(snapshot.child("game").child("questions").child("question" + i).getValue(String.class));
                    game.correctOptionQueue.add(snapshot.child("game").child("correctOptions").child("correctOption" + i).getValue(Integer.class));
                    HashSet<Integer> options = new HashSet<>();
                    for (int j = 0; j < 5; j++) {
                        options.add(snapshot.child("game").child("options").child("options"+i).child("option"+j).getValue(Integer.class));
                    }
                    game.optionsQueue.add(options);
                }

                Player player0 = snapshot.child("player0").getValue(Player.class);
                String player0Name = player0.getUsername();
                Player player1 = snapshot.child("player1").getValue(Player.class);
                String player1Name = player1.getUsername();

                if (USERNAME.equals(player0Name)) {
                    playerNumber = 0;
                    curPlayer = player0;
                    opponentPlayer = player1;
                } else if (USERNAME.equals(player1Name)) {
                    playerNumber = 1;
                    curPlayer = player1;
                    opponentPlayer = player0;
                }

                tvOpponentName.setText(opponentPlayer.getUsername());
                // show opponent info
                showOpponentInfo();
                // turn on timer
                turnOnTimer();
                // initialize next stage
                nextStage();
                // show current question
                showCurrentQuestion();
                // show current options
                showCurrentOptions();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showOpponentInfo() {
        tvOpponentTitle.setVisibility(View.VISIBLE);
        tvOpponentName.setVisibility(View.VISIBLE);
    }

    private void hideOpponentInfo() {
        tvOpponentTitle.setVisibility(View.INVISIBLE);
        tvOpponentName.setVisibility(View.INVISIBLE);
    }

    private void initGame(){
        game = new GameDesign(GAME_OPERATION, GAME_LEVEL,GAME_MODE,1,0);
        // Start the timer
        turnOnTimer();
        nextStage();
        // show current question
        showCurrentQuestion();
        // show current options
        showCurrentOptions();
    }

    private int getBonus() {
        // add bonus based on response time
        int bonus;
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        long endTime = ts.getTime();
        long duration = (endTime - game.startTime) / 1000;
        if (duration <= 2) {
            bonus = 5;
        } else if (duration <= 5) {
            bonus = 2;
        } else {
            bonus = 0;
        }
        return bonus;
    }

    /*
    To be invoked when user clicks on an answer
     */
    private void validateAnswer(Button answer, ImageView monster) {
        if (Integer.parseInt(answer.getText().toString()) == game.curAnswer) {

            //sound effect and shows a party popper
            sound.playRightSound();
            fadeOutAndHideImage((ImageView) findViewById(R.id.party_popper));


            // We do not reward answer if the correct answer picked lastly
            if (game.curOptions.size() > 1) {
                game.score += 10;
                game.score += getBonus();
            }

            tvScore.setText("Score: " + game.score);

            // add current score to online game database
            if (GAME_MODE == false) {
                curPlayer.setScore(game.score);
                rootDatabaseRef.child("Matches").child(MATCH_ID).child("player"+playerNumber).setValue(curPlayer);
            }

            if (game.curStage < 10) {
                nextStage();
            } else {
                // End Game
                endGame();
                //sound effect
                sound.playEndSound();
            }

        } else {
            game.curOptions.remove(Integer.valueOf(answer.getText().toString()));
            answer.setVisibility(View.INVISIBLE);
            monster.setVisibility(View.INVISIBLE);
            //sound effect
            sound.playWrongSound();
//            Toast toast = Toast.makeText(GameActivity.this, "Oops! Try again", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        }
    }

    private void showCurrentQuestion() {
        tvQuestion.setText(game.curQuestion);
    }

    private void showCurrentOptions() {
        Iterator iterator = game.curOptions.iterator();
        option1.setText(String.valueOf(iterator.next()));
        option2.setText(String.valueOf(iterator.next()));
        option3.setText(String.valueOf(iterator.next()));
        option4.setText(String.valueOf(iterator.next()));
        option5.setText(String.valueOf(iterator.next()));
    }

    private void nextStage() {
        // get all monsters back and renew the time
        seconds = 0;
        showAllMonsters();
        game.generateOneStage();
        tvScore.setText("Score: " + game.score);
        // show current question
        showCurrentQuestion();
        // show current options
        showCurrentOptions();
        game.curStage++;
    }

    private void hideAllMonsters() {
        option1.setVisibility(View.INVISIBLE);
        option2.setVisibility(View.INVISIBLE);
        option3.setVisibility(View.INVISIBLE);
        option4.setVisibility(View.INVISIBLE);
        option5.setVisibility(View.INVISIBLE);

        m1.setVisibility(View.INVISIBLE);
        m2.setVisibility(View.INVISIBLE);
        m3.setVisibility(View.INVISIBLE);
        m4.setVisibility(View.INVISIBLE);
        m5.setVisibility(View.INVISIBLE);
    }

    private void showAllMonsters() {
        option1.setVisibility(View.VISIBLE);
        option2.setVisibility(View.VISIBLE);
        option3.setVisibility(View.VISIBLE);
        option4.setVisibility(View.VISIBLE);
        option5.setVisibility(View.VISIBLE);

        m1.setVisibility(View.VISIBLE);
        m2.setVisibility(View.VISIBLE);
        m3.setVisibility(View.VISIBLE);
        m4.setVisibility(View.VISIBLE);
        m5.setVisibility(View.VISIBLE);
    }

    private void endGame() {
        // Set most UI components invisible
        hideAllMonsters();

        tvScore.setVisibility(View.INVISIBLE);
        tvTime.setVisibility(View.INVISIBLE);
        tvQuestion.setVisibility(View.INVISIBLE);

        hideOpponentInfo();

        if (GAME_MODE) {
            showSoloGameResult();
        } else {
            changeOnlineGameoverStatus();
            showOnlineGameResult();
        }

        m1.setVisibility(View.VISIBLE);
        m2.setVisibility(View.VISIBLE);
        m3.setVisibility(View.VISIBLE);
        m4.setVisibility(View.VISIBLE);
        m5.setVisibility(View.VISIBLE);
        storeGameScore();
    }

    private void changeOnlineGameoverStatus() {
        curPlayer.setGameOver(true);
        rootDatabaseRef.child("Matches").child(MATCH_ID).child("player"+playerNumber).setValue(curPlayer);
    }

    private void showSoloGameResult() {
        openGameResultDialog(game.getCurScore());
    }

    private void openGameResultDialog(int gameScore) {
        GameResultDialog gameResultDialog = new GameResultDialog(GAME_MODE, gameScore, personalBestFlag);
        gameResultDialog.show(getSupportFragmentManager(), "result");
    }

    /**
     * Opening a dialog telling the user in an online game to wait for his opponent to finish game.
     * @param gameScore the user's total game score
     * @param onlineGameFinished boolean value showing if the opponent in an online game has finished or not
     */
    private void openGameResultDialog(int gameScore, boolean onlineGameFinished) {
        String opponentNumber = "player" + (1 - playerNumber);
        GameResultDialog gameResultDialog = new GameResultDialog(gameScore,
                personalBestFlag,
                GAME_MODE,
                onlineGameFinished,
                opponentPlayer.getUsername(),
                opponentNumber,MATCH_ID);
        gameResultDialog.show(getSupportFragmentManager(), "result");
    }

    /**
     * Opening a dialog showing the final result of an online game.
     * @param gameScore the user's total game score
     * @param opponentScore the opponent's total game score
     */
    private void openGameResultDialog(int gameScore, int opponentScore) {
        GameResultDialog gameResultDialog = new GameResultDialog(gameScore,
                personalBestFlag,
                GAME_MODE,
                true,
                opponentPlayer.getUsername(),
                opponentScore);
        gameResultDialog.show(getSupportFragmentManager(), "result");
    }

    private void showOnlineGameResult() {
        rootDatabaseRef.child("Matches").child(MATCH_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // opponentPlayer = childSnapshot.child("player" + (1 - playerNumber)).getValue(Player.class);

                // check if the current player has higher score
                if (!snapshot.child("player" + (1 - playerNumber)).child("gameOver").getValue(Boolean.TYPE)) {
                    openGameResultDialog(game.score, false);
                } else {
                    int opponentPlayerScore = snapshot.child("player" + (1 - playerNumber)).child("score").getValue(Integer.class);
                    openGameResultDialog(game.score, opponentPlayerScore);
                    // destroy current match
                    // rootDatabaseRef.child("Matches").child(MATCH_ID).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void connectUIComponents() {
        option1 = findViewById(R.id.btnAnswer1);
        m1 = findViewById(R.id.ivMonster1);

        option2 = findViewById(R.id.btnAnswer2);
        m2 = findViewById(R.id.ivMonster2);

        option3 = findViewById(R.id.btnAnswer3);
        m3 = findViewById(R.id.ivMonster3);

        option4 = findViewById(R.id.btnAnswer4);
        m4 = findViewById(R.id.ivMonster4);

        option5 = findViewById(R.id.btnAnswer5);
        m5 = findViewById(R.id.ivMonster5);

        partyPopper = findViewById(R.id.party_popper);
        partyPopper.setVisibility(View.INVISIBLE);

        tvOpponentName = findViewById(R.id.tvCompetitorName);
        tvOpponentTitle = findViewById(R.id.tvCompetitorTitle);

        homeButton = findViewById(R.id.btnCompeteHome);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScoreCount);
        tvTime = findViewById(R.id.tvTimeCount);
    }

    private void installListeners() {

        // add onClick listener for button and image view
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswer(option1, m1);
            }
        });
        m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswer(option1, m1);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswer(option2, m2);
            }
        });
        m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswer(option2, m2);
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswer(option3, m3);
            }
        });
        m3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswer(option3, m3);
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswer(option4, m4);
            }
        });
        m4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswer(option4, m4);
            }
        });

        option5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswer(option5, m5);
            }
        });
        m5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswer(option5, m5);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GamePlayActivity.this, GameSetting.class));
            }
        });
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

    private void fadeOutAndHideImage(final ImageView img)
    {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                img.setVisibility(View.GONE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        img.startAnimation(fadeOut);
    }

    private void turnOnTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tvTime.setText("TIME: "+ seconds);
                seconds++;
            }
        }, 500, 1000);
    }

}