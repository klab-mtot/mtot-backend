package org.konkuk.klab.mtot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.konkuk.klab.mtot.domain.Location;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PinFromJourneyResponse {
    private Long pinId;
    private Location location;
    private LocalDateTime createdDate;
}
