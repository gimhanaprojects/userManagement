package com.giimhana.userManagement.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.giimhana.userManagement.domain.User;
import com.giimhana.userManagement.exception.domain.EmailExistException;
import com.giimhana.userManagement.exception.domain.UsernameExistException;

public interface UserService {

    User register(String firstName, String lastName, String username, String email)
            throws UsernameNotFoundException, UsernameExistException, EmailExistException;

    List<User> getUser();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

}
