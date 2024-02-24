package com.bezina.myNotes.Security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


import javax.crypto.SecretKey;

public class SecurityConstants {
    public static final String SIGN_UP_URLS = "/api/auth/**";
   // public static final String SECRET = "SecretKeyGenJWT"; //depricated
    public static final SecretKey SECRET_KEY =  Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING ="Authorization";
    public static final String CONTENT_TYPE = "Application/json";
    public static final long EXPIRATION_TIME = 600_000;

}
