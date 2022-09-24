package com.giimhana.userManagement.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class LoginAttemptService {
    private static final int MAXIMUM_NUMBER_OF_ATTEMPS = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private LoadingCache<String, Integer> loginAttempCache;

    public LoginAttemptService() {
        super();
        loginAttempCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(100)
                .build(new CacheLoader<String, Integer>() {

                    public Integer load(String key) {
                        return 0;
                    }

                });
    }

    public void evictUserFromLoginAttempCache(String username) {
        loginAttempCache.invalidate(username);
    }

    public void addUserToLoginAttempCache(String username) throws ExecutionException {
        int attempts = 0;

        attempts = ATTEMPT_INCREMENT + loginAttempCache.get(username);
        loginAttempCache.put(username, attempts);

    }

    public boolean hasExceededMaxAttempts(String username) throws ExecutionException {
        return loginAttempCache.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPS;
    }

}
