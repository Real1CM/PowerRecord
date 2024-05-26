package com.main.wave.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtils {

    private final String SALT = "IsSignKeyInWaveAnalyzerForJwtSaltAndDoNotTellAnyBody";
    private final String REFRESH_SALT = "IsSignKeyInWaveAnalyzerForJwtSaltRefreshTokenAndDoNotTellAnyBody";
    private final Long EXPIRATION = 30 * 60 * 1000L;
    private final Long REFRESH_EXPIRATION = 24* 60 * 60 * 1000L;

    public String generateToken(String userName) {
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,userName,false);
    }

    public String generateRefreshToken(String userName) {
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,userName,true);
    }

    public String extractUserName(String token,Boolean isRefreshToken) {
        return extractClaim(token,Claims::getSubject,isRefreshToken);
    }

    public Date extractExpiration(String token,Boolean isRefreshToken) {
        return extractClaim(token,Claims::getExpiration,isRefreshToken);
    }


    public Boolean validateToken(String token, String name,Boolean isRefreshToken) {
        String userName = extractUserName(token,isRefreshToken);
        return userName.equals(name) && !isTokenExpired(token,isRefreshToken);
    }


    private Boolean isTokenExpired(String token,Boolean isRefreshToken) {
        return extractExpiration(token,isRefreshToken).before(new Date());
    }


    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver,Boolean isRefreshToken) {
        final Claims claims = extractAllClaims(token,isRefreshToken);
        return claimsResolver.apply(claims);
    }

    private SecretKey getSignKey(Boolean isRefreshToken) {
        byte[] decode = Decoders.BASE64.decode(isRefreshToken ? REFRESH_SALT : SALT);
        return Keys.hmacShaKeyFor(decode);
    }

    private String createToken(Map<String,Object> claims,String userName,Boolean isRefreshToken) {
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (isRefreshToken ? REFRESH_EXPIRATION : EXPIRATION)))
                .signWith(getSignKey(isRefreshToken))
                .compact();
    }


    private Claims extractAllClaims(String token,Boolean isRefreshToken) {
        return Jwts.parser().verifyWith(getSignKey(isRefreshToken))
                .build().parseSignedClaims(token)
                .getPayload();
    }



}
