package org.konkuk.klab.mtot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.konkuk.klab.mtot.domain.Location;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PinInfoResponse {

    private Long pinId;
    private Location location;

}
