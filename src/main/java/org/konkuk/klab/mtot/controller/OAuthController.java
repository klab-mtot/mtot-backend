package org.konkuk.klab.mtot.controller;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.oauth.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static org.konkuk.klab.mtot.oauth.GoogleOAuthLoginSupporter.*;

@Controller
@RequiredArgsConstructor
public class OAuthController {

    private final GoogleOAuthLoginSupporter googleOAuthLoginSupporter;
    private final OAuthService oAuthService;

    @Value("${google-oauth.client-id}")
    private String CLIENT_ID;

    @GetMapping("/login/oauth")
    public String oauth(){
        return "redirect:" + ENDPOINT + "?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code&scope=" + SCOPE;
    }

    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<OAuthResponse> oAuthLogin(String code){
        GoogleUser googleUser = googleOAuthLoginSupporter.getUserFromCode(code);
        OAuthResponse oAuthResponse = oAuthService.login(googleUser);
        return ResponseEntity.ok(oAuthResponse);
    }

}
