package org.konkuk.klab.mtot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.konkuk.klab.mtot.domain.Location;

@Getter
@AllArgsConstructor
public class PinUpdateRequest {

    @NotNull(message = "journeyId를 전달해주세요.")
    private Long journeyId;

    @NotNull(message = "location을 전달해주세요.")
    private Location location;
}
