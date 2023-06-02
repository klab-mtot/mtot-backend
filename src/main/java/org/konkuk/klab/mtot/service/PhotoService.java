package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Photo;
import org.konkuk.klab.mtot.domain.Pin;
import org.konkuk.klab.mtot.dto.response.PhotoUploadResponse;
import org.konkuk.klab.mtot.exception.JourneyNotFoundException;
import org.konkuk.klab.mtot.exception.PinNotFound;
import org.konkuk.klab.mtot.exception.TeamAccessDeniedException;
import org.konkuk.klab.mtot.repository.JourneyRepository;
import org.konkuk.klab.mtot.repository.PhotoRepository;
import org.konkuk.klab.mtot.repository.PinRepository;
import org.konkuk.klab.mtot.s3.AwsS3FileSupporter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final AwsS3FileSupporter awsS3FileSupporter;
    private final PhotoRepository photoRepository;
    private final JourneyRepository journeyRepository;
    public final PinRepository pinRepository;

    @Transactional
    public PhotoUploadResponse uploadPhotos(String loginEmail, Long pinId, List<MultipartFile> multipartFiles) throws IOException {

        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(PinNotFound::new);
        Long journeyId = pin.getJourney().getId();

        Journey journey = journeyRepository.findById(journeyId)
                .orElseThrow(JourneyNotFoundException::new);

        journey.getTeam().getMemberTeams()
                .stream()
                .filter(memberTeam -> memberTeam.getMember().getEmail().equals(loginEmail))
                .findAny()
                .orElseThrow(TeamAccessDeniedException::new);

        List<Long> photoIds = new ArrayList<>();
        List<String> imageUrls = awsS3FileSupporter.uploadImages(multipartFiles);

        for(String url : imageUrls){
            Photo photo = new Photo(pin, url, LocalDate.now());
            Long photoId = photoRepository.save(photo).getId();
            photoIds.add(photoId);
        }
        return new PhotoUploadResponse(photoIds, imageUrls);
    }

}
