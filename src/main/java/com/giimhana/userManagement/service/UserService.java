package com.giimhana.userManagement.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import com.giimhana.userManagement.domain.User;
import com.giimhana.userManagement.exception.domain.EmailExistException;
import com.giimhana.userManagement.exception.domain.EmailNotFoundException;
import com.giimhana.userManagement.exception.domain.UsernameExistException;

public interface UserService {

        User register(String firstName, String lastName, String username, String email)
                        throws UsernameNotFoundException, UsernameExistException, EmailExistException,
                        MessagingException;

        List<User> getUser();

        User findUserByUsername(String username);

        User findUserByEmail(String email);

        User addNewUser(String firstName, String lastName, String username, String email, String role,
                        boolean isNotLocked,
                        boolean isActive, MultipartFile profileImage)
                        throws UsernameNotFoundException, UsernameExistException, EmailExistException, IOException;

        User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername,
                        String newEmail, String role,
                        boolean isNotLocked, boolean isActive, MultipartFile profileImage)
                        throws UsernameNotFoundException, UsernameExistException, EmailExistException, IOException;

        void deleteUser(long id);

        void resetPassword(String email) throws EmailNotFoundException, MessagingException;

        User updateProfileImage(String username, MultipartFile profileImage)
                        throws UsernameNotFoundException, UsernameExistException, EmailExistException, IOException;

}
