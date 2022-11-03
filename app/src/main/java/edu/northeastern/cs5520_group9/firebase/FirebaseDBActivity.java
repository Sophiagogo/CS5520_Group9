package edu.northeastern.cs5520_group9.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.cs5520_group9.R;

public class FirebaseDBActivity extends AppCompatActivity {
    private TextView userNameTV;
    private Button userRegisterBT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_database);
        userNameTV = findViewById(R.id.firebase_username);
        userRegisterBT = findViewById(R.id.firebase_register);

        userRegisterBT.setOnClickListener(view -> registerUserAccount());


    }

    private void registerUserAccount() {

        Intent intent = new Intent(this, SendStickerActivity.class);
        startActivity(intent);
    }
}
