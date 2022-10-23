package edu.northeastern.cs5520_group9.webService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.cs5520_group9.R;

public class WebServiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);

        final EditText etUserName = (EditText) findViewById(R.id.login_username);
        final EditText etPassword = (EditText) findViewById(R.id.login_password);

        final Button login = (Button) findViewById(R.id.Login);
        final Button register = (Button) findViewById(R.id.login_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(WebServiceActivity.this, RegisterActivity.class);
                WebServiceActivity.this.startActivity(registerIntent);
            }
        });
    }
}
