package com.example.chatApp.Services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret_key;
    private SecretKey key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret_key));
    }
    public String generateToken(UserDetails user){
        Map<String, Object> claim= new HashMap<>();
        return createToken(claim,user);
    }
    public String createToken(Map<String,Object> claim,UserDetails user){
        return Jwts.builder()
                .claims(claim)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*30))
                .signWith(key())
                .compact();
    }
    public String extractUserName(String token){
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean isTokenValid(String token, UserDetails expectedUsername){
        try{
            var claims= Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
            boolean notExpired= claims.getExpiration().after(new Date());
            return notExpired && expectedUsername.getUsername().equals(claims.getSubject());
        }
        catch(Exception e) {
            return false;
        }
    }
}
