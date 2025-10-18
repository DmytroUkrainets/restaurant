package edu.ukma.restaurant.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private final Algorithm algorithm;
    private final long ttlMinutes;

    public JwtService(
        @Value("${app.jwt.secret:change-me-dev-secret}") String secret,
        @Value("${app.jwt.ttlMinutes:1440}") long ttlMinutes // за замовчуванням 1 доба
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.ttlMinutes = ttlMinutes;
    }

    public String generate(String username, String role) {
        Instant now = Instant.now();
        return JWT.create()
            .withSubject(username)
            .withClaim("role", role)
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(now.plus(ttlMinutes, ChronoUnit.MINUTES)))
            .sign(algorithm);
    }
}
