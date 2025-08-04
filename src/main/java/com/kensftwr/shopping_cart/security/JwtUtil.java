package com.kensftwr.shopping_cart.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationInMils}")
    private int expirationTime;

    public String generateJwtToken(Authentication authentication) {

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        List<String> authorities = myUserDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList()
                ;

        return Jwts.builder()
                .setSubject(myUserDetails.getEmail())
                .claim("id",myUserDetails.getId())
                .claim("roles",myUserDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret)); // Decode Base64 secret
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }


    public boolean validateJwtToken(String token){

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e){
            throw new JwtException("Invalid token");
        }
    }




}


