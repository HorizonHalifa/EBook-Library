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
 * - Fetches ad logs the FCM token
 * - Subscribes the device to the "new_books" topic
 */
public class FirebaseManager {
    // TODO: Remove the token logging when moving away from the debug stage towards production.
    /**
     * Initializes FCM by requesting permissions (Android 13+), fetching the token,
     * and subscribing the user to the "new_books" topic.
     * @param context the current Activity context (that is used for permission and subscription)
     */
    public static void initializeFCM(Context context) {

        // Request POST_NOTIFICATIONS permission on Android 13+
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

        // Fetch FCM registration token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) {
                        Log.w("FCM_TOKEN", "Failed to fetch FCM token", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    Log.d("FCM_TOKEN", "FCM token: " + token);
                });

        // Subscribe to "new_books" topic
        FirebaseMessaging.getInstance().subscribeToTopic("new_books")
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("FCM_TOPIC", "Subscribed to topic: new_books");
                    } else {
                        Log.w("FCM_TOPIC", "Failed to subscribe to topic", task.getException());
                    }
                });


    }
}
