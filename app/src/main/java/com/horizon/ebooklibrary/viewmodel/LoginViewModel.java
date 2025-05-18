package com.horizon.ebooklibrary.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.horizon.ebooklibrary.model.LoginRequest;
import com.horizon.ebooklibrary.model.LoginResponse;
import com.horizon.ebooklibrary.repository.AuthRepository;

/**
 * ViewModel for the Login screen.
 * Handles the login logic.
 */
public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<LoginResponse> loginResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LoginViewModel() {
        this.authRepository = new AuthRepository();
    }

    /**
     * Called by the View (LoginActivity) to trigger login.
     * @param email User's email
     * @param password User's password
     */
    public void login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        authRepository.login(request, new AuthRepository.LoginCallback() {
            @Override
            public void onSuccess(LoginResponse response) {
                loginResult.postValue(response);
            }

            @Override
            public void onError(String error) {
                clearLoginResult();
                errorMessage.postValue(error);
            }
        });
    }

    /**
     * Observed by the UI to get the login result.
     */
    public LiveData<LoginResponse> getLoginResult() {
        return loginResult;
    }

    /**
     * Observed by the UI to get error messages.
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Clears the current login result.
     * Useful after the UI consumes the event to avoid repeated triggers.
     */
    public void clearLoginResult() {
        loginResult.setValue(null);
    }
}
