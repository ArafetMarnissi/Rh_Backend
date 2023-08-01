package com.example.RH.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "AjwCQt3oZ0jqmrObIH8ZUzGlcij4R6MW+NNggTx56YvsUeUOUV+eAiX36Oqn4D56MooLH1vM9aweQz+/0zl6RGTZVlW20X8ZA6l9gr6LCVc/mSfqdzREpOvZXsNE7nRxNIvSd4aiDEnFvEJMYQx08tXhBPjUrPxkggzrJ2op2773QfMRpN1kRNTbPpAn8scb/PCPAdAfIaXnbnvGs/yvEwedh6tX5afatg/VzFmH0v68ww7OYgtYbtJVRZxU3H5a6sTSysK0H5fNUr+5esVFvpFSwbCcioB/1z99cVBZuoV9oAyWySD19kT658VIGS0Id3mS0sd/Xdh7odcaYE+oFG6cSBY5WzunVjtxCMptPUE=\n";
    public String extractUserName(String token) {
        return extractClaim(token ,Claims::getSubject);
    }
    public <T> T extractClaim(String token , Function<Claims,T> claimsResolver){
        final Claims claims = ExtractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    public String generateToken(
            Map<String,Object> ExtraClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(ExtraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10000000 + 600 * 242 ))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }
    public boolean isTokenValid(String token ,UserDetails userDetails){
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims ExtractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
