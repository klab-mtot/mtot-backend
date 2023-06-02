package org.konkuk.klab.mtot.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class PhotoUploadRequest {
    private Long pinId;
    List<MultipartFile> images;
}
