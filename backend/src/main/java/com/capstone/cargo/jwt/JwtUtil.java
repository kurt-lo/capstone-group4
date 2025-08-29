package com.capstone.cargo.jwt;

import com.capstone.cargo.util.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final String RAW_SECRET =
            "CHANGE-ME-TO-A-VERY-LONG-RANDOM-SECRET-STRING-AT-LEAST-64-BYTES-LONG-THANKS-LOL-123456";
    private final SecretKey jwtSecretKey = Keys.hmacShaKeyFor(RAW_SECRET.getBytes(StandardCharsets.UTF_8));

    private static final long JWT_EXPIRATION_MS = 3_600_000L;
    private static final String ROLES_CLAIM = "roles";

    public String generateToken(String username, Collection<String> roles) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + JWT_EXPIRATION_MS);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(exp)
                .claim(ROLES_CLAIM, roles)
                .signWith(jwtSecretKey, Jwts.SIG.HS512)
                .compact();
    }

    /** From UserDetails (collects authorities → role names) */
    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority) // e.g. "ADMIN" or "ROLE_ADMIN" depending on your setup
                .map(this::normalizeAuthorityName)
                .collect(Collectors.toList());
        return generateToken(userDetails.getUsername(), roles);
    }

    private String normalizeAuthorityName(String auth) {
        if (auth == null) {
            return null;
        }
        return switch (auth) {
            case "ROLE_ADMIN", "ADMIN" -> "ADMIN";
            case "ROLE_USER", "USER"   -> "USER";
            default -> auth; // fallback, in case some other authority shows up
        };
    }


    /* --------------------
       Parsing helpers
       -------------------- */

    public String getUsernameFromToken(String token) {
        return getAllClaims(token).getSubject();
    }

    /** Alias if you still refer to it as email elsewhere */
    public String getEmailFromToken(String token) {
        return getUsernameFromToken(token);
    }

    /** Role names from token (e.g., ["ADMIN","USER"]) */
    public Set<String> getRolesFromToken(String token) {
        Claims claims = getAllClaims(token);
        Object raw = claims.get(ROLES_CLAIM);
        if (raw instanceof Collection<?> col) {
            return col.stream().map(String::valueOf).collect(Collectors.toSet());
        }
        return Set.of();
    }

    /** Roles as enums (safe parsing; ignores unknowns) */
    public Set<Role> getRoleEnumsFromToken(String token) {
        return getRolesFromToken(token).stream()
                .map(name -> {
                    try { return Role.valueOf(name); } catch (Exception e) { return null; }
                })
                .filter(r -> r != null)
                .collect(Collectors.toSet());
    }

    /* --------------------
       Validation
       -------------------- */

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    /* --------------------
       Internal
       -------------------- */

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecretKey)  // JJWT 0.12+ parser
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
