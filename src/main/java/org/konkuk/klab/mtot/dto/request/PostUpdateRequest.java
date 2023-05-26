package org.konkuk.klab.mtot.dto.request;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PostUpdateRequest {
    private Long id;
    private String Html;
}
