package com.example.tokendemo.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JwtGenerator {
    public String generateToken(String username){
        return "Bearer"+JWT.create().withAudience(username).sign(Algorithm.HMAC256("Vini-Passwd"));
    }
}
