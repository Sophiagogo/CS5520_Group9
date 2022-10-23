package edu.northeastern.cs5520_group9.webService;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import edu.northeastern.cs5520_group9.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etName = (EditText) findViewById(R.id.register_name);
        final EditText etUserName = (EditText) findViewById(R.id.register_username);
        final EditText etPassword = (EditText) findViewById(R.id.register_password);

        final Button register = (Button) findViewById(R.id.register_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = etName.getText().toString();
                final String username = etUserName.getText().toString();
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                Toast.makeText(getApplicationContext(), "Register success", Toast.LENGTH_SHORT);
//                                Intent intent = new Intent(RegisterActivity.this, WebServiceActivity.class);
//                                RegisterActivity.this.startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(), "Register fail", Toast.LENGTH_SHORT);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                registerRequset RegisterRequset = new registerRequset(name, username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(RegisterRequset);
            }
        });


    }
}