package edu.northeastern.cs5520_group9.groupProject;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class LoginPresenter {
    private LoginView view;
    private FirebaseAuth auth;
    private Handler backgroundHandler;
    private Handler mainHandler;

    public LoginPresenter(LoginView view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();

        HandlerThread handlerThread = new HandlerThread("LoginBackgroundThread");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void login(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            view.showError("Email or password cannot be empty.");
            return;
        }

        view.showProgress(true);
        backgroundHandler.post(() -> {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mainHandler.post(() -> {
                        view.showProgress(false);
                        view.navigateToGameSettings();
                    });
                } else {
                    mainHandler.post(() -> {
                        view.showProgress(false);
                        view.showError("Login failed: " + task.getException().getMessage());
                    });
                }
            });
        });
    }

    public void onDestroy() {
        if (backgroundHandler != null) {
            backgroundHandler.getLooper().quitSafely();
        }
    }
}

