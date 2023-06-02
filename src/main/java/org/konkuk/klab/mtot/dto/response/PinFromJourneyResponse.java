package org.konkuk.klab.mtot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.konkuk.klab.mtot.domain.Location;
import org.konkuk.klab.mtot.domain.Photo;

import java.util.List;

@Getter
@AllArgsConstructor
public class PinFromJourneyResponse {
    private Long pinId;
    private Long journeyId;
    private Location location;
    private List<Photo> photos;
}
