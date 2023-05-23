package org.konkuk.klab.mtot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendshipUpdateRequest {
    private String requesterEmail;
    private String receiverEmail;
    private Boolean accept;
}
