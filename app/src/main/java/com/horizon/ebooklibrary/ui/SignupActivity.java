package com.horizon.ebooklibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.horizon.ebooklibrary.R;
import com.horizon.ebooklibrary.viewmodel.SignupViewModel;

/**
 * UI for user signup screen, handles user input and delegates signup logic to the SignupViewModel.
 */
public class SignupActivity extends AppCompatActivity {

    //private EditText editTextName, editTextEmail, editTextPassword;
    private EditText editTextEmail, editTextPassword;
    private Button buttonSignup, buttonBack;
    private SignupViewModel signupViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupViewModel();
        setLanguageFilters();
        setupListeners();
        observeViewModel();
    }

    /**
     * Binds all view references from the layout
     */
    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignup = findViewById(R.id.buttonSignup);
        buttonBack = findViewById(R.id.buttonBack);
    }

    /**
     * Sets up the view model for the class and connects the needed listeners and observers
     */
    private void setupViewModel() {
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
    }


    /**
     * Sets up button listeners
     */
    private void setupListeners() {
        buttonSignup.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            signupViewModel.signup(email, password);
        });

        buttonBack.setOnClickListener(v -> finish());
    }

    /**
     * Make sure that users can only use english for logins and signups
     */
    private void setLanguageFilters() {
        editTextEmail.setFilters(new InputFilter[] {
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char c = source.charAt(i);
                        if (!Character.isLetterOrDigit(c) && c != '@' && c != '.' && c != '_') {
                            return "";
                        }
                    }
                    return null;
                }
        });

        editTextPassword.setFilters(new InputFilter[] {
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char c = source.charAt(i);
                        if (!Character.isLetterOrDigit(c) && c != '@' && c != '.' && c != '_') {
                            return "";
                        }
                    }
                    return null;
                }
        });
    }

    /**
     * Sets up the view model for the class and connects the needed listeners and observers
     */
    private void observeViewModel() {
        signupViewModel.getSignupSuccess().observe(this, success -> {
            if(success != null && success) {
                showToast("Account created successfully!");
                signupViewModel.clearSignupSuccess();
                navigateToLogin();
            }
        });

        signupViewModel.getErrorMessage().observe(this, error -> {
            if(error != null) {
                showToast(error);
            }
        });
    }

    /**
     * Shows a message to the user
     * @param message to display
     */
    private void showToast(String message) {
        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Navigates to LoginActivity
     */
    private void navigateToLogin() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}