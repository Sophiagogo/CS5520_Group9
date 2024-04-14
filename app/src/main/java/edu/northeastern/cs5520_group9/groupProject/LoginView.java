package edu.northeastern.cs5520_group9.groupProject;

public interface LoginView {
    void showProgress(boolean show);
    void showError(String message);
    void navigateToGameSettings();
}
