package com.example.web_project_1.config;

import com.example.web_project_1.model.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret:${JWT_KEY}}")
    private String secretKey;

    @Value("${jwt.access.expiration:900000}")
    private long accessExpirationInMs;

    @Value("${jwt.refresh.expiration:604800000}")
    private long refreshExpirationInMs;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String username, Set<Role> roles, Long sessionId) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles.stream().map(Enum::name).collect(Collectors.toList()));
        claims.put("type", "ACCESS");
        claims.put("sessionId", sessionId);

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessExpirationInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String username, Long sessionId) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("sessionId", sessionId);
        claims.put("type", "REFRESH");

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshExpirationInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getTokenType(String token) {
        return (String) Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("type");
    }

    public Long getSessionId(String token) {
        return ((Number) Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("sessionId")).longValue();
    }
}