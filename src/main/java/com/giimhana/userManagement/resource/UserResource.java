package com.giimhana.userManagement.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giimhana.userManagement.domain.User;
import com.giimhana.userManagement.exception.domain.EmailExistException;
import com.giimhana.userManagement.exception.domain.ExceptionHandling;
import com.giimhana.userManagement.exception.domain.UsernameExistException;
import com.giimhana.userManagement.service.UserService;

@RestController
@RequestMapping(path = { "", "/user" })
public class UserResource extends ExceptionHandling {

    private UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user)
            throws EmailExistException, UsernameNotFoundException, UsernameExistException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUserName(),
                user.getEmail());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }
}
