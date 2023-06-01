package org.konkuk.klab.mtot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;

@Getter
@AllArgsConstructor
public class CalenderThumbnailResponse {
    HashMap<LocalDate, String> map = new HashMap<>();
}
