package org.konkuk.klab.mtot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditPostRequest {
    private Long journeyId;
    private String title;
    private String article;
}
