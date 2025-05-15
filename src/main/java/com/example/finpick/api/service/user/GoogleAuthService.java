package com.example.finpick.api.service.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.finpick.domain.user.User;
import com.example.finpick.domain.user.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleAuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${jwt.secretKey}")
    private String jwtSecret;

    private final UserRepository userRepository;

    public GoogleAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String verifyAndCreateJwt(String idTokenString) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");

            // DB에 사용자 저장/조회
            User user = userRepository.findByUsername(email)
                    .orElseGet(() -> userRepository.save(new User(email)));

            // JWT 발급
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                    .withSubject(email)
                    .withClaim("name", name)
                    .withClaim("picture", picture)
                    .sign(algorithm);
        } else {
            throw new IllegalArgumentException("Invalid ID token.");
        }
    }
}