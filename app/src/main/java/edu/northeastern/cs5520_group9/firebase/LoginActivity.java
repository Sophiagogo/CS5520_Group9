package edu.northeastern.cs5520_group9.firebase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.northeastern.cs5520_group9.R;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences globalLoginData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        globalLoginData = getSharedPreferences("login", MODE_PRIVATE);
    }

    public void login(View view) {
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("MainActivity", "onCreate Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    System.out.println("token from add for " + username +  ":" + token);
                    @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    User user = new User(username, deviceId, token);
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child("users").child(username).setValue(user);
                });
        globalLoginData.edit().putString("username", username).apply();
        Intent intent = new Intent(this, SendStickerActivity.class);
        startActivity(intent);
    }
}
