package edu.northeastern.cs5520_group9.groupProject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.northeastern.cs5520_group9.R;

public class GroupProjectActivity extends AppCompatActivity {
    Button login_btn, register_btn;
    FirebaseUser firebaseUser;


    @Override
    protected void onStart() {

        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_project);
        login_btn = findViewById(R.id.Login);
        register_btn = findViewById(R.id.Start_Register);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GroupProjectActivity.this, Login_for_game.class));
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GroupProjectActivity.this, Register_for_game.class));
            }
        });
    }
}
