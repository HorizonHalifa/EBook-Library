package com.horizon.ebooklibrary.viewmodel;

import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.horizon.ebooklibrary.model.SignupRequest;
import com.horizon.ebooklibrary.service.AuthService;
import com.horizon.ebooklibrary.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * ViewModel for the signup screen.
 * Handles user signup logic and exposes success or error states as observable liveData.
 */
public class SignupViewModel extends ViewModel{

    private final AuthService authService;

    // LiveData to observe success or failure
    private final MutableLiveData<Boolean> signupSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SignupViewModel() {
        authService = ApiClient.getClient().create(AuthService.class);
    }

    public void signup(String email, String password) {

        // Basic Validation before sending request
        if(!validateInput(email, password)) {
            return; // Stop here if the input is invalid.
        }


        SignupRequest request = new SignupRequest(email, password);

        authService.signup(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()) {
                    signupSuccess.postValue(true); // Notify UI of success
                } else if(response.code() == 409 || response.code() == 403) {
                    errorMessage.postValue("Email already in use");
                } else {
                    errorMessage.postValue("Signup failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * @return LiveData to observe signup success state
     */
    public LiveData<Boolean> getSignupSuccess() {
        return signupSuccess;
    }

    /**
     * @return LiveData to observe my error messages
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Resets the success state after it is handled
     */
    public void clearSignupSuccess() {
        signupSuccess.setValue(null);
    }

    /**
     * Validates the email and password fields.
     * @param email the user's email
     * @param password the user's password
     * @return true if valid; otherwise false
     */
    private boolean validateInput(String email, String password) {
        if(email == null || email.trim().isEmpty()) {
            errorMessage.postValue("Email cannot be empty.");
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage.postValue("Please enter a valid email address.");
            return false;
        }

        if(password == null || password.trim().isEmpty()) {
            errorMessage.postValue("Password cannot be empty");
            return false;
        }

        return true;
    }
}
