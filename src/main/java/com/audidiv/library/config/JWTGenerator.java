package com.audidiv.library.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTGenerator {
    byte[] keyBytes = Decoders.BASE64.decode(SecurityConstant.JWT_SECRET);
    SecretKey key = Keys.hmacShaKeyFor(keyBytes);

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currenDate = new Date();
        Date expirDate = new Date(currenDate.getTime() + SecurityConstant.JWT_EXPIRATION);

        String token = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(expirDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
        return token;
    }

    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT has expired or invalidated");
        }
    }
}
