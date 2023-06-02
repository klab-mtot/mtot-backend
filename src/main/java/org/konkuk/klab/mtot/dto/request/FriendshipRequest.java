package org.konkuk.klab.mtot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipRequest {

    @NotNull(message = "상대 이메일은 필수 사항입니다.")
    private String receiverEmail;
}
