package edu.northeastern.cs5520_group9.firebase;

import androidx.annotation.NonNull;

/**
 * This is the Sticker class which define a sticker object.
 */

public class Sticker implements Comparable<Sticker> {
    public int id;
    public String fromUser;
    public String toUser;
    public String sendTime;
    public String receivedTime;

    public Sticker(int id, String fromUser, String toUser, String sendTime) {
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.sendTime = sendTime;
    }

    // implement getter

    public String getKey() {
        return id + " from: " + fromUser + " to: " + toUser + " send time: " + sendTime;
    }

    public int getId() {
        return id;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public String getSendTime() {
        return sendTime;
    }

    // sort sticker
    @Override
    public int compareTo(Sticker other) {
        return this.sendTime.compareTo(other.getSendTime());
    }
}