package edu.northeastern.cs5520_group9.groupProject.model;

public class Score {
    private int score;
    private String username;

    public Score(int score, String username) {
        this.score = score;
        this.username = username;
    }

    public Score() {}

    public int getScore() {
        return score;
    }

    public String getUsername() {
        return username;
    }
}