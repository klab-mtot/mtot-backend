package org.konkuk.klab.mtot.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.response.CalenderThumbnailResponse;
import org.konkuk.klab.mtot.dto.response.GetAllPhotoUrlResponse;
import org.konkuk.klab.mtot.dto.response.PhotoUploadResponse;
import org.konkuk.klab.mtot.oauth.LoginMemberEmail;
import org.konkuk.klab.mtot.service.PhotoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/calendarThumbnail")
    private ResponseEntity<CalenderThumbnailResponse> getCalenderThumbnailLink(@LoginMemberEmail String email,
                                                                               @RequestParam(name = "year") int year,
                                                                               @RequestParam(name = "month") @Min(1) @Max(12) int month){
        CalenderThumbnailResponse response = photoService.findThumbnail(email, year, month);
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoUploadResponse> uploadPhotos2aws(@LoginMemberEmail String email,
                                                                @RequestParam(value = "photos") List<MultipartFile> photos,
                                                                @RequestParam(value = "pinId") Long pinId) throws IOException {
        PhotoUploadResponse response = photoService.uploadPhotos(email, pinId, photos);
        return ResponseEntity.ok(response);
    }
}
