package org.konkuk.klab.mtot.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OAuthResponse {
    String accessToken;
    String email;
}
