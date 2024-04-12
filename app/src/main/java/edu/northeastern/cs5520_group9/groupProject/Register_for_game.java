import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import edu.northeastern.cs5520_group9.groupProject.GameSetting;
import edu.northeastern.cs5520_group9.groupProject.RegisterPresenter;

public class Register_for_game extends AppCompatActivity implements RegisterView {
    TextInputEditText username_txt, email_txt, password_txt;
    Button register_button;
    ProgressBar progressBar;

    RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_game);

        username_txt = findViewById(R.id.register_username);
        email_txt = findViewById(R.id.register_email);
        password_txt = findViewById(R.id.register_password);
        progressBar = findViewById(R.id.progressBar);
        register_button = findViewById(R.id.register_button);

        presenter = new RegisterPresenter(this);

        register_button.setOnClickListener(view -> {
            String text_username = username_txt.getText().toString().trim();
            String text_email = email_txt.getText().toString().trim();
            String text_password = password_txt.getText().toString().trim();

            if (TextUtils.isEmpty(text_username) || TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_password)) {
                Toast.makeText(Register_for_game.this, "You missed something", Toast.LENGTH_SHORT).show();
            } else {
                presenter.register(text_username, text_email, text_password);
            }
        });
    }

    @Override
    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccess() {
        Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToGameSettings() {
        Intent intent = new Intent(this, GameSetting.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
