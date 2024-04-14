package edu.northeastern.cs5520_group9.groupProject.model;

public interface RegisterView {
    void showProgress(boolean show);
    void showError(String error);
    void showSuccess();
    void navigateToGameSettings();
}
