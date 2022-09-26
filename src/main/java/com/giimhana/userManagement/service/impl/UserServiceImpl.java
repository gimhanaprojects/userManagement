package com.giimhana.userManagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;

import com.giimhana.userManagement.constant.UserImplConstant;
import com.giimhana.userManagement.domain.User;
import com.giimhana.userManagement.domain.UserPrincipal;
import com.giimhana.userManagement.enumeration.Role;
import com.giimhana.userManagement.exception.domain.EmailExistException;
import com.giimhana.userManagement.exception.domain.UsernameExistException;
import com.giimhana.userManagement.repository.UserRepository;
import com.giimhana.userManagement.service.EmailService;
import com.giimhana.userManagement.service.LoginAttemptService;
import com.giimhana.userManagement.service.UserService;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private LoginAttemptService loginAttemptService;
    private EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
            LoginAttemptService loginAttemptService, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            LOGGER.error("User not found by username: " + username);
            throw new UsernameNotFoundException(UserImplConstant.NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDate(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(UserImplConstant.FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }

    }

    private void validateLoginAttempt(User user) {
        if (user.isNotLocked()) {
            if (loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setNotLocked(false);

            } else {

                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttempCache(user.getUsername());
        }
    }

    @Override
    public User register(String firstName, String lastName, String username, String email)
            throws UsernameNotFoundException, UsernameExistException, EmailExistException, MessagingException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
        User user = new User();
        user.setUserId(generateUserId());
        String password = generatePassword();
        String encordedPassword = encordedPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encordedPassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl());

        userRepository.save(user);
        LOGGER.info("New user password " + password);
        // emailService.sendNewPasswordEmail(firstName, password, email);

        return user;

    }

    private String getTemporaryProfileImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(UserImplConstant.DEFAULT_USER_IMAGE_PATH)
                .toUriString();
    }

    private String encordedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
            throws UsernameNotFoundException, UsernameExistException, EmailExistException {

        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);

        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UsernameNotFoundException(UserImplConstant.NO_USER_FOUND_BY_USERNAME + currentUsername);
            }

            if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(UserImplConstant.USERNAME_ALREADY_EXISTS);
            }

            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(UserImplConstant.EMAIL_ALREADY_EXISTS);
            }

            return currentUser;
        } else {

            if (userByNewUsername != null) {
                throw new UsernameExistException(UserImplConstant.USERNAME_ALREADY_EXISTS);
            }

            if (userByNewEmail != null) {
                throw new EmailExistException(UserImplConstant.EMAIL_ALREADY_EXISTS);
            }

            return null;

        }
    }

    @Override
    public List<User> getUser() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User addNewUser(String firstName, String lastName, String username, String email, String role,
            boolean isNotLocked, boolean isActive, MultipartFile profileImage) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername,
            String newEmail, String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteUser(long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resetPassword(String email) {
        // TODO Auto-generated method stub

    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) {
        // TODO Auto-generated method stub
        return null;
    }

}
