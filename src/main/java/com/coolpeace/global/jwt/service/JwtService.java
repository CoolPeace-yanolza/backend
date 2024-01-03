package com.coolpeace.global.jwt.service;

import com.coolpeace.global.jwt.dto.JwtPair;
import com.coolpeace.global.jwt.dto.JwtPayload;
import com.coolpeace.global.jwt.exception.JwtExpiredAuthorizationException;
import com.coolpeace.global.jwt.exception.JwtInvalidSignatureException;
import com.coolpeace.global.jwt.exception.JwtMalformedStructureException;
import com.coolpeace.global.jwt.exception.JwtUnsupportedFormatException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private static final String CLIENT_ID_KEY = "client_id";
    private static final String CLIENT_EMAIL_KEY = "client_email";

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${service.jwt.refresh-expiration}")
    private Long refreshExpiration;

    private final SecretKey secretKey;

    public JwtService(@Value("${service.jwt.secret-key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public JwtPair createTokenPair(JwtPayload jwtPayload) {
        return JwtPair.from(createAccessToken(jwtPayload), createRefreshToken(jwtPayload), accessExpiration);
    }

    public JwtPair createTokenPair(String accessToken, String refreshToken) {
        return JwtPair.from(accessToken, refreshToken, accessExpiration);
    }

    public void deleteRefreshToken(String email) {
        // TODO: 저장된 리프레시 토큰 삭제
    }

    public JwtPair refreshAccessToken(String refreshToken, JwtPayload jwtPayload) {
        String accessToken = createToken(jwtPayload, accessExpiration);
        return JwtPair.from(accessToken, refreshToken, accessExpiration);
    }

    public JwtPayload verifyAccessToken(String accessToken) {
        return verifyToken(accessToken, false);
    }

    public JwtPayload verifyRefreshToken(String refreshToken) {
        return verifyToken(refreshToken, true);
    }

    public String createAccessToken(JwtPayload jwtPayload) {
        return createToken(jwtPayload, accessExpiration);
    }

    public String createRefreshToken(JwtPayload jwtPayload) {
        return createToken(jwtPayload, refreshExpiration);
    }

    private String createToken(JwtPayload jwtPayload, long expiration) {
        return Jwts.builder()
                .claim(CLIENT_ID_KEY, jwtPayload.id())
                .claim(CLIENT_EMAIL_KEY, jwtPayload.email())
                .issuer(issuer)
                .issuedAt(jwtPayload.issuedAt())
                .expiration(new Date(jwtPayload.issuedAt().getTime() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public JwtPayload verifyToken(String jwtToken, boolean isStrict) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();
            return JwtPayload.from(
                    claims.get(CLIENT_ID_KEY, String.class),
                    claims.get(CLIENT_EMAIL_KEY, String.class),
                    claims.getIssuedAt());
        } catch (ExpiredJwtException e) {
            if (!isStrict) {
                return JwtPayload.from(
                        e.getClaims().get(CLIENT_ID_KEY, String.class),
                        e.getClaims().get(CLIENT_EMAIL_KEY, String.class),
                        e.getClaims().getIssuedAt());
            }
            throw new JwtExpiredAuthorizationException();
        } catch (MalformedJwtException e) {
            throw new JwtMalformedStructureException();
        } catch (SignatureException e) {
            throw new JwtInvalidSignatureException();
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            throw new JwtUnsupportedFormatException();
        }
    }
}
