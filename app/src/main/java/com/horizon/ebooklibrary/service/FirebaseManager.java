package com.horizon.ebooklibrary.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Handles Firebase Cloud Messaging setup:
 * - Requests notification permissions on Android 13+
 * - Fetches the FCM token
 * - Subscribes the device to the "new_books" topic
 */
public class FirebaseManager {

    // private static final String TAG = "FCM_INIT"; For Logcat
    // private static final String NEW_BOOK_TOPIC = "new_books"; For Logcat

    /**
     * Initializes FCM setup:
     * Requests permissions if needed, subscribes to topic and logs token.
     * @param context the current Activity context (that is used for permission and subscription)
     */
    public static void initializeFCM(Context context) {

     requestNotificationPermissionIfNeeded(context);
     subscribeToTopic();

    }

    // Requests permissions for notifications
    private static void requestNotificationPermissionIfNeeded(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                if(context instanceof Activity) {

                    ((Activity) context).requestPermissions(
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            1001
                    );
                }
            }
        }
    }

    // Subscribe to "new_books" topic
    private static void subscribeToTopic() {
        FirebaseMessaging.getInstance(). subscribeToTopic("new_books")
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("FCM_TOPIC", "Subscribed to topic: new_books");
                    } else {
                        Log.w("FCM_TOPIC", "Failed to subscribe to topic", task.getException());
                    }
                });
    }

}
