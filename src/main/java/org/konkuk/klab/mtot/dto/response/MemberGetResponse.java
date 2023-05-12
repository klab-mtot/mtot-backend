package org.konkuk.klab.mtot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberGetResponse {
    private Long id;
    private String name;
    private String email;
}
