package com.horizon.ebooklibrary.service;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessaging;
public class FirebaseTokenService extends FirebaseMessagingService{

    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("FCM_TOKEN", "New token: " + token);
        // TODO: Send this token to the backend via authenticated POST request
    }

    // Force token refresh
    public static void printCurrentToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM_TOKEN", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM token
                    String token = task.getResult();
                    Log.d("FCM_TOKEN", "Current token: " + token);
                });
    }
}
