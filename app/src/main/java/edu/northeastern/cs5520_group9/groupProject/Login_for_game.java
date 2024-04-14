package edu.northeastern.cs5520_group9.groupProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.northeastern.cs5520_group9.R;


public class Login_for_game extends AppCompatActivity implements LoginView {
    TextInputEditText email, password;
    Button login_btn;
    ProgressBar progressBar;
    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_for_game);

        progressBar = findViewById(R.id.login_progressBar);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_btn);

        presenter = new LoginPresenter(this);

        login_btn.setOnClickListener(view -> {
            String email_text = email.getText().toString().trim();
            String password_text = password.getText().toString().trim();
            presenter.login(email_text, password_text);
        });
    }

    @Override
    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToGameSettings() {
        Intent intent = new Intent(this, GameSetting.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

}
