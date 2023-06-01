package org.konkuk.klab.mtot.oauth;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.konkuk.klab.mtot.exception.TokenExpiredException;
import org.konkuk.klab.mtot.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String secretKey;
    private final long tokenValidPeriod;
    private final JwtParser jwtParser;
    private final String headerPrefix;

    public JwtService(@Value("${jwt.secretKey}") String secretKey,
                      @Value("${header}") String headerPrefix,
                      @Value("${jwt.tokenValidPeriod}") long tokenValidPeriod){
        this.secretKey = secretKey;
        this.tokenValidPeriod = tokenValidPeriod;
        this.headerPrefix = headerPrefix;
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
            throw new TokenExpiredException();
        } catch (JwtException e){
            throw new InvalidTokenException();
        }
    }

    public String getTokenFromRequest(HttpServletRequest request){
        String header = request.getHeader(headerPrefix);
        if (header == null)
            throw new InvalidTokenException();
        if (header.toLowerCase().startsWith("bearer ") && header.split(" ").length == 2)
            return header.split(" ")[1];
        throw new InvalidTokenException();
    }

    public String getPayload(String token){
        try{
            return jwtParser.parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e){
            throw new TokenExpiredException();
        } catch (JwtException e){
            throw new InvalidTokenException();
        }
    }

}
