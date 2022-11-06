package edu.northeastern.cs5520_group9.firebase;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.northeastern.cs5520_group9.R;

public class FCMActivity extends FirebaseMessagingService {
    private String userName;
    private String userId;
    private DatabaseReference databaseReference;
    private static final String TAG = "Firebase Cloud Message";
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String CHANNEL_NAME = "CHANNEL_NAME";
    private static final String CHANNEL_DESCRIPTION = "CHANNEL_DESCRIPTION";
    private static final Map<Integer, Integer> stickers = new HashMap<>();

    @SuppressLint("HardwareIds")
    public void onCreate() {
        super.onCreate();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(userId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                HashMap tempMap = (HashMap) task.getResult().getValue();
                if (tempMap == null) {
                    Log.e("firebase", "Empty data");
                    return;
                }
                userName = Objects.requireNonNull(tempMap.get("username")).toString();
            }
        });
        userId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        stickers.put(1, R.drawable.apple);
        stickers.put(2, R.drawable.banana);
        stickers.put(3, R.drawable.bean_stew);
        stickers.put(4, R.drawable.noodles);
        stickers.put(5, R.drawable.sandwich);
        stickers.put(6, R.drawable.sushi_roll);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.i(TAG, "onTokenRefresh completed with token: " + token);

        // new token
        @SuppressLint("HardwareIds")
        User user = new User(this.userName, Settings.Secure.getString(
                getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID), token);
        databaseReference.child("users").child(user.getDeviceId()).setValue(user);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("Msg", "Message received ["+remoteMessage+"]");
        // Check if message contains a data payload
        Map<String, String> message = remoteMessage.getData();
        if (message.size() > 0) {
            Log.d(TAG, "Message data payload: " + message);
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            if (notification != null)
                // Send a notification when got a new message
                sendNotification(remoteMessage, Integer.parseInt((Objects.requireNonNull
                                (remoteMessage.getData().get("image_id")))));
        }
    }

    /**
     * Create and show a notification containing the received FCM message
     */
    private void sendNotification(RemoteMessage remoteMessage, int stickerId) {
        Intent intent = new Intent(this, SendStickerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Create the pending intent to launch the activity
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                1410, intent, PendingIntent.FLAG_ONE_SHOT);

        // Create the notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_group)
                .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
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
