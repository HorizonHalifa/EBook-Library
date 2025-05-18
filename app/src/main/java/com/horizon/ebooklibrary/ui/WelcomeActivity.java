package com.horizon.ebooklibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.horizon.ebooklibrary.R;
import com.horizon.ebooklibrary.service.FirebaseManager;
import com.horizon.ebooklibrary.util.SessionManager;
import com.horizon.ebooklibrary.util.TokenManager;

import java.util.Locale;

/*
 * Displays Sign In and Create Account buttons.
 * Navigates to the Login or Signup Screens when clicked
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        // Initialization of the SessionManager and TokenManager in the application
        TokenManager.init(getApplicationContext());
        SessionManager.init(getApplicationContext());

        // Rest of android studio auto generated onCreate:
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        }); // End of android studio auto generate OnCreate.

        // Call to the FCM setup
        FirebaseManager.initializeFCM(this);

        // Button click handlers
        Button buttonSignIn = findViewById(R.id.buttonSignIn);
        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        // Navigate to Login Screen
        buttonSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Navigate to Signup Screen
        buttonCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}