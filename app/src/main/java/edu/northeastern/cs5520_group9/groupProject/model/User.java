package edu.northeastern.cs5520_group9.groupProject.model;

import java.util.HashMap;

public class User {
    private String id;
    private String username;
    private String token;
    public int personalBestScoreEasy;
    public int personalBestScoreHard;
    public int personalBestScoreMedium;
    public int numOfGamesPlayed;

    public User(String id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
        numOfGamesPlayed = 0;
        personalBestScoreEasy = 0;
        personalBestScoreMedium = 0;
        personalBestScoreHard = 0;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getNumOfGamesPlayed() {
        return numOfGamesPlayed;
    }

    public String getToken() {
        return token;
    }
}