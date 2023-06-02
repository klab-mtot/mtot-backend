package org.konkuk.klab.mtot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PendingFriendshipResponse {
    private Long friendshipId;
    private String requesterName;
    private String requesterEmail;
}
