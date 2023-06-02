package org.konkuk.klab.mtot.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.request.PhotoUploadRequest;
import org.konkuk.klab.mtot.dto.response.CalenderThumbnailResponse;
import org.konkuk.klab.mtot.dto.response.GetAllPhotoUrlResponse;
import org.konkuk.klab.mtot.dto.response.PhotoUploadResponse;
import org.konkuk.klab.mtot.oauth.LoginMemberEmail;
import org.konkuk.klab.mtot.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/photo")
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping("/pin/{id}")
    private ResponseEntity<GetAllPhotoUrlResponse> getLinkByPin(@LoginMemberEmail String email,
                                                                @PathVariable("id") Long pinId){
        GetAllPhotoUrlResponse response = photoService.getAllPhotosUrlByPinId(email, pinId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/journey/{id}")
    private ResponseEntity<GetAllPhotoUrlResponse> getLinkByJourney(@LoginMemberEmail String email,
                                                                    @PathVariable("id") Long journeyId){
        GetAllPhotoUrlResponse response = photoService.getAllPhotosByJourneyId(email, journeyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/calenderThumbnail")
    private ResponseEntity<CalenderThumbnailResponse> getCalenderThumbnailLink(@LoginMemberEmail String email,
                                                                               @RequestParam(name = "year") int year,
                                                                               @RequestParam(name = "month") @Min(1) @Max(12) int month){
        CalenderThumbnailResponse response = photoService.findThumbnail(email, year, month);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PhotoUploadResponse> uploadPhotos2aws(@LoginMemberEmail String email,
                                                                @RequestBody PhotoUploadRequest request) throws IOException {
        PhotoUploadResponse response = photoService.uploadPhotos(email, request.getPinId(), request.getImages());
        return ResponseEntity.ok(response);
    }
}
