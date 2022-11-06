package edu.northeastern.cs5520_group9.firebase;

public class User {
    private String username;
    private String deviceId;
    private String token;

    public User(String username, String deviceId, String token) {
        this.username = username;
        this.deviceId = deviceId;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
