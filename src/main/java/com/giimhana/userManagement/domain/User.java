package com.giimhana.userManagement.domain;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    private String[] roles;// ROLE_USER,ROLE_ADMIN
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;

}
