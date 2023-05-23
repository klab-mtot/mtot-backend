package org.konkuk.klab.mtot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendshipAcceptCheckRequest {
    private String requesterEmail;
    private String receiverEmail;
}
