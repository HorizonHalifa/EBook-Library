package com.horizon.ebooklibrary.service;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.horizon.ebooklibrary.R;

/**
 * Handles incoming FCM messages. Displays a system notification when a message is received.
 */
public class FirebaseTokenService extends FirebaseMessagingService{

    private static final String TAG = "FCM_RECEIVED";
    private static final String CHANNEL_ID = "default";

    /**
     * Called when a new FCM token is generated
     * @param token The token used for sending messages to this application instance. This token is
     *     the same as the one retrieved by {@link FirebaseMessaging#getToken()}.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("TAG", "New FCM token generated");
    }

    /**
     * Called when a new FCM message is received.
     * This method will be invoked for both data and notification messages, while the app is in the foreground.
     * @param remoteMessage Remote message that has been received.
     */
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if(remoteMessage.getNotification() != null) {
           String title = remoteMessage.getNotification().getTitle();
           String body = remoteMessage.getNotification().getBody();

           showNotification(title, body);
        }
    }

    /**
     * Displays a notification for a received message
     * @param title the title of the notification to display
     * @param body the body of the notification to display
     */
    private void showNotification(String title, String body) {
        // Ensure notification channel exists (Android 8+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "New Book Alerts", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title != null ? title : "New Book")
                .setContentText(body != null ? body : "Check out the latest book!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Check permission before showing notification (Android 13+)
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU ||
                checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(this).notify(1, builder.build());
        } else {
            Log.w(TAG, "Notification permission not granted.");
        }
    }

}
