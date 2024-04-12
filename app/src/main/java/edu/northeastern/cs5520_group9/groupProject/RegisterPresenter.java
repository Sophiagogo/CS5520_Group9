package edu.northeastern.cs5520_group9.groupProject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterPresenter {
    private RegisterView view;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void register(String username, String email, String password) {
        view.showProgress(true);
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                assert firebaseUser != null;
                String userId = firebaseUser.getUid();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", userId);
                hashMap.put("username", username);
                hashMap.put("numberOfGamePlayed", 0);
                hashMap.put("Best_score_at_easy", 0);
                hashMap.put("Best_score_at_medium", 0);
                hashMap.put("Best_score_at_hard", 0);

                databaseReference.child("User").child(username).setValue(hashMap).addOnCompleteListener(task1 -> {
                    view.showProgress(false);
                    if (task1.isSuccessful()) {
                        view.showSuccess();
                        view.navigateToGameSettings();
                    } else {
                        view.showError("Register error");
                    }
                });
            } else {
                view.showProgress(false);
                view.showError("Authentication failed.");
            }
        });
    }
}

