package com.horizon.ebooklibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
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
import com.horizon.ebooklibrary.util.TokenManager;
import com.horizon.ebooklibrary.viewmodel.LoginViewModel;


/**
 * LoginActivity is the UI for the user login.
 * It interacts with LoginViewModel and observes login state via LiveData.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize token manager
        TokenManager.init(getApplicationContext());

        // Setup UI
        initViews();
        setupViewModel();
        setLanguageFilters();
        setupListeners();
        observeViewModel();
    }

    /**
     * Finds and assigns view references from layout
     */
    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
    }

    private void setupViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    /**
     * Set up the onClick listeners for the login button.
     */
    private void setupListeners() {
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if(email.isEmpty() || password.isEmpty()) {
                showToast("Please enter both email and password.");
                return;
            }

            loginViewModel.login(email, password);
        });
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
     * Observes login results from the ViewModel class:
     * If login response is successful, clear the cache and navigate the user to the next screen.
     * If there is an error, display it.
     */
    private void observeViewModel() {
        loginViewModel.getLoginResult().observe(this, loginResponse -> {
            if(loginResponse != null) {
                Log.d("LOGIN_RESPONSE", "Role from response: " + loginResponse.getRole());
                TokenManager.getInstance().saveLoginData(loginResponse);
                showToast("Login successful!");
                navigateToMainScreen();
               loginViewModel.clearLoginResult(); // clears results after success
            }
        });

        loginViewModel.getErrorMessage().observe(this, error -> {
            if(error != null) {
                showToast(error);
            }
        });
    }

    /**
     * Navigates to the main screen after successful login.
     */
    private void navigateToMainScreen() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Displays a short message to the user.
     * @param message The message to display
     */
    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Navigates to the Signup screen when the "Go to Signup" button is clicked.
     */
    public void goToSignup(android.view.View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}