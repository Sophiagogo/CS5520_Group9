package edu.northeastern.cs5520_group9.groupProject;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import edu.northeastern.cs5520_group9.R;

public class Sound {

    private static SoundPool soundPool;
    private static int hitSound;
    private static int wrongSound;
    private static int rightSound;
    private static int endSound;

    public Sound(Context context) {

        // Create the soundPool
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(attrs)
                .build();

        // Load hit, wrong, right, end sounds
        hitSound = soundPool.load(context, R.raw.hit, 1);
        wrongSound = soundPool.load(context, R.raw.wrong, 1);
        rightSound = soundPool.load(context, R.raw.right, 1);
        endSound = soundPool.load(context, R.raw.end, 1);
    }

    // Play Hit Sound
    public void playHitSound() {
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    // Play Wrong Sound
    public void playWrongSound() {
        soundPool.play(wrongSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    // Play Right Sound
    public void playRightSound() {
        soundPool.play(rightSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    // Play End Sound
    public void playEndSound() {
        soundPool.play(endSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}