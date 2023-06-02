package org.konkuk.klab.mtot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendshipInfoResponse {
    private Long friendshipId;
    private Long memberId;
    private String name;
    private String email;
}
