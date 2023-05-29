package org.konkuk.klab.mtot.oauth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleUser {
    private String id;
    private String email;
    private String verified_email;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String locale;
    @Builder
    public GoogleUser(String email, String name, String picture){
        this.email = email;
        this.name = name;
        this.picture = picture;
    }
}
