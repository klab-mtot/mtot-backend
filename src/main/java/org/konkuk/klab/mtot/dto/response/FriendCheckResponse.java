package org.konkuk.klab.mtot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.konkuk.klab.mtot.domain.Friendship;

import java.util.List;

@Getter
@AllArgsConstructor
public class FriendCheckResponse {
    private List<Friendship> friendshipList; // 친구 목록
}
