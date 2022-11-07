package edu.northeastern.cs5520_group9.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.cs5520_group9.R;

public class SendStickerActivity extends AppCompatActivity {
    private Integer selectedImageId;
    private Spinner usernameSelector;
    private String myUsername;
    private Map<String, User> userMap = new HashMap<>();
    private List<ImageView> imageViews = new ArrayList<>();
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);

        // load current logged in username from global data store
        myUsername = getSharedPreferences("login", MODE_PRIVATE).getString("username", "unknownUser");

        // images
        imageViews.add(findViewById(R.id.image1));
        imageViews.add(findViewById(R.id.image2));

        // prepare spinner
        usernameSelector = findViewById(R.id.usernameList);
        db = FirebaseDatabase.getInstance().getReference();
        db.child("users").get().addOnCompleteListener((task) -> {
            userMap = (Map) task.getResult().getValue();
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(userMap.keySet().toArray()));
            usernameSelector.setAdapter(adapter);
        });
    }

    public void sendSticker(View view) {
        String friendUsername = usernameSelector.getSelectedItem().toString();
        Sticker sticker = new Sticker(selectedImageId, myUsername, friendUsername, new Date().toString());
        db.child("stickers").child(sticker.getKey()).setValue(sticker).addOnSuccessListener(
                (task) -> {
                    Toast toast = Toast.makeText(getApplicationContext(), "Sticker sent successfully", Toast.LENGTH_SHORT);
                    toast.show();
                });
        //new Thread(() -> sendMessageToDevice(userMap.get(friendUsername).getToken(), sticker)).start();
    }

    public void clickImage(View view) {
        for (int i = 0; i < imageViews.size(); i++) {
            if (imageViews.get(i).getId() != view.getId()) {
                // unselect any other image
                imageViews.get(i).setColorFilter(null);
            } else {
                // select current image
                selectedImageId = i + 1;
                imageViews.get(i).setColorFilter(ContextCompat.getColor(this.getApplicationContext(), R.color.purple_200), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    private void showHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
}