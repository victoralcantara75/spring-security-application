package com.secutiry.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SecurityUtil {
    
    private static final int ACCESS_EXPIRATION_TIME = 10 * 60 * 1000;
    private static final int REFRESH_EXPIRATION_TIME = 30 * 60 * 1000;


    public static Algorithm getJwtAlgorithm(){
        return Algorithm.HMAC256("secret".getBytes());
    }

    public static String createAccessJwt(HttpServletRequest request, String username, List<String> authorities){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date( System.currentTimeMillis() + ACCESS_EXPIRATION_TIME ))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", authorities)
                .sign(SecurityUtil.getJwtAlgorithm());
    }

    public static String createRefreshJwt(HttpServletRequest request, String username){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date( System.currentTimeMillis() + REFRESH_EXPIRATION_TIME ))
                .withIssuer(request.getRequestURL().toString())
                .sign(SecurityUtil.getJwtAlgorithm());
    }

    public static DecodedJWT decodeJwt(String token){
        Algorithm algorithm = SecurityUtil.getJwtAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }

    public static Collection<SimpleGrantedAuthority> rolesToSimpleAuthorities(Stream<String> roles){
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return authorities;
    }

    public static Map<String, String> getJsonTokens(String accessToken, String refreshToken){
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }

    public static void writeResponseBody(HttpServletResponse response, Map<String, String> json) throws IOException{
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), json);
    }
}
