package org.konkuk.klab.mtot.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.OAuthAttributes;
import org.konkuk.klab.mtot.domain.PrincipalDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.jwt.authorities-key}")
    private String AUTHORITIES_KEY;

    @Value("${spring.jwt.token-validity-in-seconds}")
    private long tokenValidityInSeconds;
    private Key key;

    private final MemberService memberService;

    @PostConstruct
    protected void init(){
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email){
        //generate payload
        //registered claims
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + tokenValidityInSeconds);
        Claims claims = Jwts.claims()
                .setSubject("access_token")
                .setIssuedAt(now)
                .setExpiration(expirationDate);

        //private claims
        claims.put("email", email);
        claims.put("role", "ROLE_USER");

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean verifyToken(String token){
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().after(new Date());
        }catch (Exception e){
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request){
        String authorizationHeader =  request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token){
        String email = (String) Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email");
        PrincipalDetails principalDetails = memberService.loadMemberByUsername(email);
        return new UsernamePasswordAuthenticationToken(principalDetails, "",principalDetails.getAuthorities());
    }
}
