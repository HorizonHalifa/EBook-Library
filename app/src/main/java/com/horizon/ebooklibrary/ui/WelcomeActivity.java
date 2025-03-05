package com.horizon.ebooklibrary.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.horizon.ebooklibrary.R;

/*
 * Displays Sign In and Create Account buttons.
 * Navigates to the Login or Signup Screens when clicked
 */
public class WelcomeActivity extends AppCompatActivity {
    private Button buttonSignIn, buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        // Navigate to Login Screen
        buttonSignIn.setOnClickListener(v -> {
            // TODO: Implement LoginActivity
        });

        // Navigate to Signup Screen
        buttonCreateAccount.setOnClickListener(v -> {
            // TODO: Implement SignupActivity
        });
    }
}