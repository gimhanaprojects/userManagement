package com.giimhana.userManagement.listner;

import java.util.concurrent.ExecutionException;

import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

import com.giimhana.userManagement.service.LoginAttemptService;

public class AuthenticationFaliureListner {

    private LoginAttemptService loginAttemptService;

    public AuthenticationFaliureListner(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    public void onAuthenticationFaliure(AuthenticationFailureBadCredentialsEvent event) throws ExecutionException {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof String) {
            String username = (String) event.getAuthentication().getPrincipal();
            loginAttemptService.addUserToLoginAttempCache(username);
        }
    }

}
