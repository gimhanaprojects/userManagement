package com.giimhana.userManagement.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giimhana.userManagement.exception.domain.EmailExistException;
import com.giimhana.userManagement.exception.domain.ExceptionHandling;

@RestController
@RequestMapping(path = { "", "/user" })
public class UserResource extends ExceptionHandling {

    @GetMapping("/home")
    public String showUser() throws EmailExistException {
        // return "application works";
        throw new EmailExistException("This email address already taken");
    }
}
