package org.konkuk.klab.mtot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamCreateRequest {

    @NotNull(message = "멤버 ID는 필수 입력 사항입니다.")
    private Long memberId;

    @NotNull(message = "그룹 이름은 필수 입력 사항입니다.")
    private String teamName;

}
