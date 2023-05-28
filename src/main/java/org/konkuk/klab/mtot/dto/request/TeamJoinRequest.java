package org.konkuk.klab.mtot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamJoinRequest {
    private Long memberId;
    private Long teamId;
}

