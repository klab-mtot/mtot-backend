package org.konkuk.klab.mtot.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GoogleOAuthLoginSupporter {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    public static final String ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    public static final String REDIRECT_URI = "http://localhost:8080/login/oauth2/code/google";
    public static final String SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";

    @Value("${google-oauth.client-id}")
    private String CLIENT_ID;

    @Value("${google-oauth.client-secret}")
    private String SECRET;

    public GoogleUser getUserFromCode(String code){
        System.out.println(code);
        ResponseEntity<String> googleTokenResponse = getGoogleAccessTokenByCode(code);
        try {
            GoogleToken googleToken = objectMapper.readValue(googleTokenResponse.getBody(), GoogleToken.class);
            ResponseEntity<String> googleGetResponse = createGetRequest(googleToken.getAccessToken());
            return objectMapper.readValue(googleGetResponse.getBody(), GoogleUser.class);
        } catch(JsonProcessingException e){
            e.printStackTrace();
            throw new RuntimeException("Json Error");
        }
    }

    private ResponseEntity<String> getGoogleAccessTokenByCode(String code){
        String url = "https://oauth2.googleapis.com/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", SECRET);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("grant_type", "authorization_code");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    }

    private ResponseEntity<String> createGetRequest(String googleToken){
        String url = "https://www.googleapis.com/oauth2/v1/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }
}
