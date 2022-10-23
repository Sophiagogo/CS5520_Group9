package edu.northeastern.cs5520_group9.webService;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import edu.northeastern.cs5520_group9.R;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        final EditText etName = (EditText) findViewById(R.id.profile_name);
        final EditText etUsername = (EditText) findViewById(R.id.profile_username);
    }
}