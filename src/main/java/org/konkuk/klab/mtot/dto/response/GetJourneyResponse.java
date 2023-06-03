package org.konkuk.klab.mtot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetJourneyResponse {
    private Long journeyId;
    private String name;
    private PostInfoResponse post;
    private List<PinInfoResponse> pins;
}
