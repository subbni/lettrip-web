package com.lettrip.lettripbackend.security.jwt;

import com.lettrip.lettripbackend.security.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    @Value("${jwt.exp.msec}")
    private long TOKEN_EXPIRE_MSEC;
    @Value("${jwt.secret.key}")
    private String TOKEN_SECRET_KEY;

    private Key secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(TOKEN_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 토큰 생성
     * HEADER : alg, kid
     * PAYLOAD : sub, iat, exp
     * SIGNATURE : Secret Key로 HS512 해시
     */
    public String createToken(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Date now = new Date();
        return Jwts.builder()
                .setSubject(customUserDetails.getId().toString())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ TOKEN_EXPIRE_MSEC))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

    }

    public Long getUserIdFromToken(String token) {
         return Long.parseLong(
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build().parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
