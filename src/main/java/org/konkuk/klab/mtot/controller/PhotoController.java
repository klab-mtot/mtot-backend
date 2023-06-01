package org.konkuk.klab.mtot.controller;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.response.PhotoUploadResponse;
import org.konkuk.klab.mtot.s3.AwsS3FileSupporter;
import org.konkuk.klab.mtot.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<PhotoUploadResponse> uploadPhotos2aws(@RequestBody String loginEmail, Long pinId, List<MultipartFile> multipartFiles) throws IOException {
        PhotoUploadResponse response = photoService.uploadPhotos(loginEmail,pinId,multipartFiles);
        return ResponseEntity.ok(response);
    }
}
