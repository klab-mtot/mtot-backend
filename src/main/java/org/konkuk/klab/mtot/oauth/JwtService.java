package org.konkuk.klab.mtot.oauth;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String secretKey;
    private final long tokenValidPeriod;
    private final JwtParser jwtParser;

    public JwtService(@Value("${jwt.secretKey}") String secretKey,
                      @Value("${jwt.tokenValidPeriod}") long tokenValidPeriod){
        this.secretKey = secretKey;
        this.tokenValidPeriod = tokenValidPeriod;
        this.jwtParser = Jwts.parser().setSigningKey(secretKey);
    }

    public String generateToken(String payload){
        Date now = new Date();
        Date validUntil = new Date(now.getTime() + tokenValidPeriod);
        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(validUntil)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public void verifyToken(String token){
        try{
            jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e){
            throw new RuntimeException("토큰이 만료되었습니다.");
        } catch (JwtException e){
            throw new RuntimeException("인증되지 않은 토큰입니다.");
        }
    }

    public String getTokenFromRequest(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if (header == null)
            throw new RuntimeException("토큰이 유효하지 않습니다.");

        if (header.toLowerCase().startsWith("bearer ") && header.split(" ").length == 2){
            return header.split(" ")[1];
        }
        throw new RuntimeException("토큰이 유효하지 않습니다.");
    }

    public String getPayload(String token){
        try{
            return jwtParser.parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e){
            throw new RuntimeException("토큰이 만료되었습니다.");
        } catch (JwtException e){
            throw new RuntimeException("인증되지 않은 토큰입니다.");
        }
    }
}
