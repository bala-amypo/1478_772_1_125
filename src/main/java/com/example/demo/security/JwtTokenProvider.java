package com.example.demo.security;

import com.example.demo.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
        private final long validityInMs;

            public JwtTokenProvider(
                        @Value("${jwt.secret}") String secret,
                                    @Value("${jwt.expiration}") long validityInMs) {

                                            this.key = Keys.hmacShaKeyFor(secret.getBytes());
                                                    this.validityInMs = validityInMs;
                                                        }

                                                            public String generateToken(Authentication authentication, User user) {
                                                                    Date now = new Date();
                                                                            Date expiryDate = new Date(now.getTime() + validityInMs);

                                                                                    return Jwts.builder()
                                                                                                    .setSubject(user.getEmail())
                                                                                                                    .setIssuedAt(now)
                                                                                                                                    .setExpiration(expiryDate)
                                                                                                                                                    .signWith(key, SignatureAlgorithm.HS256)
                                                                                                                                                                    .compact();
                                                                                                                                                                        }

                                                                                                                                                                            public String getUsernameFromToken(String token) {
                                                                                                                                                                                    return Jwts.parserBuilder()
                                                                                                                                                                                                    .setSigningKey(key)
                                                                                                                                                                                                                    .build()
                                                                                                                                                                                                                                    .parseClaimsJws(token)
                                                                                                                                                                                                                                                    .getBody()
                                                                                                                                                                                                                                                                    .getSubject();
                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                            public boolean validateToken(String token) {
                                                                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                                                                                Jwts.parserBuilder()
                                                                                                                                                                                                                                                                                                                    .setSigningKey(key)
                                                                                                                                                                                                                                                                                                                                        .build()
                                                                                                                                                                                                                                                                                                                                                            .parseClaimsJws(token);
                                                                                                                                                                                                                                                                                                                                                                        return true;
                                                                                                                                                                                                                                                                                                                                                                                } catch (Exception e) {
                                                                                                                                                                                                                                                                                                                                                                                            return false;
                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                        