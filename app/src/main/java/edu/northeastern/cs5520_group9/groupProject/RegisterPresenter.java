package edu.northeastern.cs5520_group9.groupProject;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterPresenter {
    private RegisterView view;
    private FirebaseAuth auth;
    private Handler backgroundHandler;
    private Handler mainHandler;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
        HandlerThread handlerThread = new HandlerThread("RegisterBackground");
        handlerThread.start();
        this.backgroundHandler = new Handler(handlerThread.getLooper());
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void register(final String username, final String email, final String password) {
        view.showProgress(true);
        backgroundHandler.post(() -> {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userId = firebaseUser.getUid();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", userId);
                    hashMap.put("username", username);
                    hashMap.put("numberOfGamePlayed", 0);
                    hashMap.put("Best_score_at_easy", 0);
                    hashMap.put("Best_score_at_medium", 0);
                    hashMap.put("Best_score_at_hard", 0);

                    databaseReference.child("User").child(username).setValue(hashMap).addOnCompleteListener(task1 -> {
                        mainHandler.post(() -> {
                            view.showProgress(false);
                            if (task1.isSuccessful()) {
                                view.showSuccess();
                                view.navigateToGameSettings();
                            } else {
                                view.showError("Register error");
                            }
                        });
                    });
                } else {
                    mainHandler.post(() -> {
                        view.showProgress(false);
                        view.showError("Authentication failed.");
                    });
                }
            });
        });
    }

    public void onDestroy() {
        if (backgroundHandler.getLooper() != null) {
            backgroundHandler.getLooper().quit();
        }
    }
}
