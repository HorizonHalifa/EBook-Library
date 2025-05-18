package com.horizon.ebooklibrary.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.horizon.ebooklibrary.ui.LoginActivity;

/**
 * SessionManager handles user session terminator across the app.
 * It ensures a clean logout by:
 * - Clearing all stored tokens via TokenManager
 * - Redirecting the user to LoginActivity on the main UI thread
 * <p>
 * This class is used in cases where background threads (In this case namely the TokenAuthenticator)
 * need to trigger a logout safely and globally.
 */
public class SessionManager {

    private static SessionManager instance;
    private final Context appContext;
    private final Handler mainHandler;

    // Constructor
    private SessionManager(Context context) {
        this.appContext = context.getApplicationContext(); // Ensures no activity leak
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Initializes the SessionManager with application context
     */
    public static void init(Context context) {
        if(instance == null) {
            instance = new SessionManager(context);
        }
    }

    /**
     * @return The instance of SessionManager
     */
    public static SessionManager getInstance() {
        if(instance == null) {
            throw new IllegalStateException("SessionManager is not initialized. Call init(context) first.");
        }
        return instance;
    }

    /**
     * Logs out the user:
     * - Clears stored tokens.
     * - Starts LoginActivity on the main thread.
     */
    public void logout() {
        // Clear stored tokens
        TokenManager.getInstance().clearTokens();

        // Navigate to LoginActivity on the main thread
        mainHandler.post(() -> {
            Intent intent = new Intent(appContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            appContext.startActivity(intent);
        });
    }
}
