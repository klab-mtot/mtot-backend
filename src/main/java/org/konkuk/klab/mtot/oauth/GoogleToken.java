package org.konkuk.klab.mtot.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleToken {
    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;

    public String getAccessToken() {
        return access_token;
    }
}

