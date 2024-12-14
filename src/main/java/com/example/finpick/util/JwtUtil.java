package com.example.finpick.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secretKey}")  // application.yml에서 값을 가져옴
    private String secretKey;

    public String generateToken(String username, String level) {
        return JWT.create()
                .withSubject(username)
                .withClaim("level", level) // level 추가
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String extractUsername(String token) {
        try {
            DecodedJWT jwt = decodeToken(token);
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid token: unable to extract username", e);
        }
    }

    // level 가져오기
    public String getLevelFromToken(String token) {
        try {
            DecodedJWT jwt = decodeToken(token);
            return jwt.getClaim("level").asString(); // level 반환
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid token: unable to extract level", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            decodeToken(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}