package edu.northeastern.cs5520_group9.groupProject;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import edu.northeastern.cs5520_group9.R;


public class Sound {

    private static SoundPool soundPool;
    private static int hitSound;
    private static int sadSound;
    private static int happySound;
    private static int endSound;


    public Sound(Context context) {

        //SoundPoll (int maxStreams, int streamType, int srcQuality)
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        hitSound = soundPool.load(context, R.raw.hit, 1);
        sadSound = soundPool.load(context, R.raw.sad, 1);
        happySound = soundPool.load(context, R.raw.happy, 1);
        endSound = soundPool.load(context, R.raw.end, 1);
    }

    public void playHitSound() {
        //play(int soundID, float leftVolume, int priority, int loop, float rate)
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playSadSound() {
        //play(int soundID, float leftVolume, int priority, int loop, float rate)
        soundPool.play(sadSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playHappySound() {
        //play(int soundID, float leftVolume, int priority, int loop, float rate)
        soundPool.play(happySound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playEndSound() {
        //play(int soundID, float leftVolume, int priority, int loop, float rate)
        soundPool.play(endSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}