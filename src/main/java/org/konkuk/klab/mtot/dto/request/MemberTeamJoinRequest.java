package org.konkuk.klab.mtot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberTeamJoinRequest {
    @NotNull(message = "그룹 ID는 필수 입력 사항입니다.")
    private Long teamId;

    @NotNull(message = "멤버 Email은 필수 입력 사항입니다.")
    private String memberEmail;
}
