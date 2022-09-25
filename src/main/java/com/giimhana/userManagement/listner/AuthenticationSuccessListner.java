package com.giimhana.userManagement.listner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.giimhana.userManagement.domain.UserPrincipal;
import com.giimhana.userManagement.service.LoginAttemptService;

@Component
public class AuthenticationSuccessListner {
    private LoginAttemptService loginAttemptService;

    @Autowired
    public AuthenticationSuccessListner(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Object pricipal = event.getAuthentication().getPrincipal();
        if (pricipal instanceof UserPrincipal) {
            UserPrincipal user = (UserPrincipal) event.getAuthentication().getPrincipal();
            loginAttemptService.evictUserFromLoginAttempCache(user.getUsername());
        }
    }

}
