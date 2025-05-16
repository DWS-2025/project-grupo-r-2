package com.spartanwrath.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final long JWT_EXPIRATION_IN_MS = 5400000;
    private static final long REFRESH_TOKEN_EXPIRATION_MSEC = 10800000L;

    @Autowired
    private UserDetailsService userDetailsService;

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SignatureException ex) {
            LOG.debug("Invalid JWT Signature");
        } catch (MalformedJwtException ex) {
            LOG.debug("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            LOG.debug("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOG.debug("Unsupported JWT exception");
        } catch (IllegalArgumentException ex) {
            LOG.debug("JWT claims string is empty");
        }
        return false;
    }

    public Token generateToken(UserDetails user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_IN_MS);

        String token = Jwts.builder()
                .subject(user.getUsername())
                .claim("auth", user.getAuthorities().stream()
                        .map(s -> new SimpleGrantedAuthority("ROLE_" + s.getAuthority()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .compact();

        return new Token(Token.TokenType.ACCESS, token, expiryDate.getTime(),
                LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
    }

    public Token generateRefreshToken(UserDetails user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_MSEC);

        String token = Jwts.builder()
                .subject(user.getUsername())
                .claim("auth", user.getAuthorities().stream()
                        .map(s -> new SimpleGrantedAuthority("ROLE_" + s.getAuthority()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .compact();

        return new Token(Token.TokenType.REFRESH, token, expiryDate.getTime(),
                LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
    }
}