package org.konkuk.klab.mtot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreateRequest {

    @NotNull(message = "그룹 이름은 필수 입력 사항입니다.")
    private String teamName;

}
