package com.giimhana.userManagement.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giimhana.userManagement.constant.SecurityConstant;
import com.giimhana.userManagement.domain.User;
import com.giimhana.userManagement.domain.UserPrincipal;
import com.giimhana.userManagement.exception.domain.EmailExistException;
import com.giimhana.userManagement.exception.domain.ExceptionHandling;
import com.giimhana.userManagement.exception.domain.UsernameExistException;
import com.giimhana.userManagement.service.UserService;
import com.giimhana.userManagement.utility.JWTTokenProvider;

@RestController
@RequestMapping(path = { "", "/user" })
public class UserResource extends ExceptionHandling {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserResource(UserService userService, AuthenticationManager authenticationManager,
            JWTTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {

        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername((user.getUsername()));
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);

    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user)
            throws EmailExistException, UsernameNotFoundException, UsernameExistException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getEmail());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
        return headers;
    }

    private void authenticate(String username, String password) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    }

}
