package edu.northeastern.cs5520_group9.groupProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

import edu.northeastern.cs5520_group9.R;

public class Register_for_game extends AppCompatActivity {
    TextInputEditText username_txt, email_txt, password_txt;
    Button register_button;

    ProgressBar progressBar;

    FirebaseAuth auth;
    DatabaseReference databaseReference;

    public static final Pattern VALID_EMAIL_ADDRESS = Pattern.compile("^[A-Z0-9._%+=]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_game);



        username_txt = findViewById(R.id.register_username);
        email_txt = findViewById(R.id.register_email);
        password_txt = findViewById(R.id.register_password);
        progressBar = findViewById(R.id.progressBar);
        register_button = findViewById(R.id.register_button);

        auth = FirebaseAuth.getInstance();

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_username = username_txt.getText().toString().trim();
                String text_email = email_txt.getText().toString().trim();
                String text_password = password_txt.getText().toString().trim();

                if (TextUtils.isEmpty(text_username) || TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_password)) {
                    Toast.makeText(Register_for_game.this, "You missed something", Toast.LENGTH_SHORT).show();
                } else if(!valid_username(text_username) || !valid_email(text_email) || !valid_password(text_password)){
                    Toast.makeText(Register_for_game.this, "You enter something wrong", Toast.LENGTH_SHORT).show();
                } else {
                    register(text_username, text_email, text_password);
                    Intent intent = new Intent(Register_for_game.this, GameSetting.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    private void register(String username, String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String user_id = firebaseUser.getUid();

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", user_id);
                    hashMap.put("username", username);
                    hashMap.put("numberOfGamePlayed", 0);
                    hashMap.put("Best_score_at_easy", 0);
                    hashMap.put("Best_score_at_medium", 0);
                    hashMap.put("Best_score_at_hard", 0);
                    databaseReference.child("User").child(username).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register_for_game.this, "Account created", Toast.LENGTH_SHORT).show();

                                finish();
                            } else {
                                Toast.makeText(Register_for_game.this, "Register error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });





                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }




    private boolean valid_username(String username) {
        final boolean[] res = {true};
        FirebaseDatabase.getInstance().getReference("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child_data: snapshot.getChildren()){
                    String cur_username = child_data.getKey();
                    if (cur_username.equals(username)){
                        res[0] = false;
                        username_txt.setError("Username already existed");
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return res[0];
    }

    private boolean valid_email(String email) {
        if (!VALID_EMAIL_ADDRESS.matcher(email).find()) {
            email_txt.setError("Please enter a valid email");
        } else{
            email_txt.setError(null);
            return true;
        }
        return false;
    }

    private boolean valid_password(String password) {
        if (password.length() < 6){
            password_txt.setError("Password should longer than 6 characters");
        }else{
            return true;
        }
        return false;
    }


}