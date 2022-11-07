package edu.northeastern.cs5520_group9.firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        User user = new User(username);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users").child(username).setValue(user);
        globalLoginData.edit().putString("username", username).apply();
        Intent intent = new Intent(this, SendStickerActivity.class);
        startActivity(intent);
    }
}
