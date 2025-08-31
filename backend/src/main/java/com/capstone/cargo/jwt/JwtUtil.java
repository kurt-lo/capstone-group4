package com.capstone.cargo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey jwtSecretKey;
    private final long jwtExpirationMs;

    public JwtUtil(@Value("${security.jwt.secret}") String secret,
                   @Value("${security.jwt.ttl-ms}") long jwtExpirationMs) {
        this.jwtSecretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(String username, String role, Map<String, Object> extraClaims) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .claim("role", role);

        if (extraClaims != null) {
            builder.addClaims(extraClaims);
        }

        return builder.signWith(jwtSecretKey, SignatureAlgorithm.HS512).compact();
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Object roleObject = claims.get("role");
        return roleObject != null ? roleObject.toString() : null;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
