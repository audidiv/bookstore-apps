package com.audidiv.library.config;

import java.security.Key;
import java.util.Date;

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
    Key key = Keys.hmacShaKeyFor(keyBytes);
    
    public String generateToken(Authentication authentication){

        String username = authentication.getName();

        String token = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SECRET)
            .compact();
            
        return token;
    }

    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
            .setSigningKey(SecurityConstant.JWT_SECRET)
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SecurityConstant.JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT has expired or invalidated");
        }
    }
}
