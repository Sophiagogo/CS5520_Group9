package edu.northeastern.cs5520_group9.firebase;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.cs5520_group9.R;

public class SendStickerActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String CHANNEL_NAME = "CHANNEL_NAME";
    private static final String CHANNEL_DESCRIPTION = "CHANNEL_DESCRIPTION";

    private Integer selectedImageId;
    private Spinner usernameSelector;
    private String myUsername;
    private long lastVisitedEpochSecond;
    private Map<String, Map<String, String>> usersMap = new HashMap<>();
    private List<ImageView> imageViews = new ArrayList<>();
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);

        // images
        imageViews.add(findViewById(R.id.image1));
        imageViews.add(findViewById(R.id.image2));

        // prepare spinner
        usernameSelector = findViewById(R.id.usernameList);
        db = FirebaseDatabase.getInstance().getReference();
        db.child("users").get().addOnCompleteListener((task) -> {
            usersMap = (Map) task.getResult().getValue();
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(usersMap.keySet().toArray()));
            usernameSelector.setAdapter(adapter);
        });

        // attach a listener to monitor a new sticker creation
        DatabaseReference stickersRef = FirebaseDatabase.getInstance().getReference("stickers");
        stickersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // load current logged in username from global data store
                myUsername = getSharedPreferences("login", MODE_PRIVATE).getString("username", "unknownUser");
                lastVisitedEpochSecond = getSharedPreferences("login", MODE_PRIVATE).getLong("lastVisitedEpochSecond", Instant.now().getEpochSecond());

                for (DataSnapshot stickerSnapshot: dataSnapshot.getChildren()) {
                    Sticker sticker = stickerSnapshot.getValue(Sticker.class);
                    if (sticker.getToUser().equals(myUsername) && sticker.getSendTimeEpochSecond() > lastVisitedEpochSecond) {
                        System.out.println("A sticker had been sent to current user: " + sticker);
                        sendNotification(sticker);
                    }
                }

                // update last visited time for the user, so the user only receives sticker that is sent beyond this time
                Instant now = Instant.now();
                getSharedPreferences("login", MODE_PRIVATE).edit().putLong("lastVisitedEpochSecond", now.getEpochSecond()).apply();
                db.child("users").child(myUsername).setValue(new User(myUsername, now.getEpochSecond()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // no-op
            }
        });
    }

    public void sendSticker(View view) {
        // image selection validation
        if (selectedImageId == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select a sticker to send", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        String friendUsername = usernameSelector.getSelectedItem().toString();
        Sticker sticker = new Sticker(selectedImageId, myUsername, friendUsername, Instant.now().getEpochSecond());
        db.child("stickers").child(String.valueOf(sticker.getSendTimeEpochSecond())).setValue(sticker).addOnSuccessListener(
                (task) -> {
                    Toast toast = Toast.makeText(getApplicationContext(), "Sticker sent successfully", Toast.LENGTH_SHORT);
                    toast.show();
                });
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

    public void showHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    /**
     * Create and show a notification for a newly sent sticker message
     */
    private void sendNotification(Sticker sticker) {
        Intent intent = new Intent(this, SendStickerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Create the pending intent to launch the activity
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                1410, intent, PendingIntent.FLAG_MUTABLE);

        // Create the notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_group)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),
                        sticker.getImageId() == 1 ? R.drawable.panda : R.drawable.polar_bear)).bigLargeIcon(BitmapFactory.decodeResource(getResources(),
                        sticker.getImageId() == 1 ? R.drawable.panda : R.drawable.polar_bear)))
                .setContentTitle("You have a new sticker from " + sticker.getFromUser() + "!")
                .setContentText("imageId=" + sticker.getImageId())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .addAction(R.mipmap.ic_launcher_group, "Snooze", pendingIntent);

        // Set the notification channel and build the notification
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription(CHANNEL_DESCRIPTION);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(0, builder.build());
    }
}