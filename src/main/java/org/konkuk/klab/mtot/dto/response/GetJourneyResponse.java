package org.konkuk.klab.mtot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.konkuk.klab.mtot.domain.Post;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetJourneyResponse {
    private Long journeyId;
    private String name;
    private Post post;
    private List<PinInfoResponse> pins;
}
