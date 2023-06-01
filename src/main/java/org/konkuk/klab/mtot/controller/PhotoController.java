package org.konkuk.klab.mtot.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Pin;
import org.konkuk.klab.mtot.dto.response.CalenderThumbnailResponse;
import org.konkuk.klab.mtot.dto.response.PhotoLinksResponse;
import org.konkuk.klab.mtot.dto.response.PhotoUploadResponse;
import org.konkuk.klab.mtot.s3.AwsS3FileSupporter;
import org.konkuk.klab.mtot.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping("/pin")
    private ResponseEntity<PhotoLinksResponse> getLinkByPin(@RequestBody @Valid Pin pin){
        PhotoLinksResponse response = photoService.findPhotoByPin(pin);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Journey")
    private ResponseEntity<PhotoLinksResponse> getLinkByJourney(@RequestBody @Valid Journey journey){
        PhotoLinksResponse response = photoService.findPhotoByJourney(journey);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/calenderThumbnail")
    private ResponseEntity<CalenderThumbnailResponse> getCalenderThumbnailLink(
            @RequestBody @Valid @RequestParam("year") int year, @RequestParam("month") int month){
        CalenderThumbnailResponse response = photoService.findThumbnail(year,month);;
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PhotoUploadResponse> uploadPhotos2aws(@RequestBody @Valid  String loginEmail, Long pinId, List<MultipartFile> multipartFiles) throws IOException {
        PhotoUploadResponse response = photoService.uploadPhotos(loginEmail,pinId,multipartFiles);
        return ResponseEntity.ok(response);
    }
}
