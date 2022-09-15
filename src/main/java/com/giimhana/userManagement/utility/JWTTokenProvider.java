package com.giimhana.userManagement.utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.giimhana.userManagement.constant.SecurityConstant;
import com.giimhana.userManagement.domain.UserPrincipal;

public class JWTTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(UserPrincipal userPrincipal){
        String[] claims = getClaimFromUser(userPrincipal);

        return JWT.create().withIssuer(SecurityConstant.GET_ARRAYS_LLC).withAudience(SecurityConstant.GET_ARRAYS_ADMINISTRATION).withIssuedAt(new Date()).withSubject(userPrincipal.getUsername()).withArrayClaim(SecurityConstant.AUTHORITIES, claims).withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME)).sign(Algorithm.HMAC512(secret.getBytes()))
    }

    private String[] getClaimFromUser(UserPrincipal user) {

        List<String> authorities = new ArrayList<String>();

        for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
            authorities.add(grantedAuthority.getAuthority());

        }

        return authorities.toArray(new String[0]);

    }

}
