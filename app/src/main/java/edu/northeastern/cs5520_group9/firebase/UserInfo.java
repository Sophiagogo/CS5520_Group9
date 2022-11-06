package edu.northeastern.cs5520_group9.firebase;

public class UserInfo {
    public String userName;
    public String deviceID;
    public String token;

    public UserInfo(String userName, String deviceID, String token) {
        this.userName = userName;
        this.deviceID = deviceID;
        this.token = token;
    }
}
