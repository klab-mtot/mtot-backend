package org.konkuk.klab.mtot.dto.request;

import lombok.Getter;

@Getter
public class EditPostRequest {
    private Long journeyId;
    private String title;
    private String article;
}
