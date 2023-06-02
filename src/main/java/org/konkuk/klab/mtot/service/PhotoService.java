package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.Photo;
import org.konkuk.klab.mtot.domain.Pin;
import org.konkuk.klab.mtot.dto.response.*;
import org.konkuk.klab.mtot.exception.JourneyNotFoundException;
import org.konkuk.klab.mtot.exception.MemberNotFoundException;
import org.konkuk.klab.mtot.exception.PinNotFound;
import org.konkuk.klab.mtot.exception.TeamAccessDeniedException;
import org.konkuk.klab.mtot.repository.JourneyRepository;
import org.konkuk.klab.mtot.repository.MemberRepository;
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
    private final PinRepository pinRepository;
    private final MemberRepository memberRepository;


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

    @Transactional(readOnly = true)
    public CalenderThumbnailResponse findThumbnail(String email, int year, int month){
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        LocalDate startDate = LocalDate.of(year, month,1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        
        // TODO: Member가 가진 Journey 가져오기 필요
        List<DayThumbnailResponse> dayThumbnailResponses = photoRepository.getThumbnailPhotosBetween(member.getId(), startDate, endDate)
                .stream()
                .map(photo -> new DayThumbnailResponse(photo.getUploadDate().getDayOfMonth(), photo.getImageUrl()))
                .toList();
        return new CalenderThumbnailResponse(dayThumbnailResponses);
    }

    @Transactional(readOnly = true)
    public GetAllPhotoUrlResponse getAllPhotosUrlByPinId(String email, Long pinId) {

        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Pin pin = pinRepository.findById(pinId).orElseThrow(PinNotFound::new);
        pin.getJourney().getTeam().getMemberTeams()
                .stream()
                .filter(memberTeam -> memberTeam.getMember().getId().equals(member.getId()))
                .findAny()
                .orElseThrow(TeamAccessDeniedException::new);

        List<PhotoUrlResponse> photoUrlResponses = photoRepository.findAllByPinId(pinId)
                .stream()
                .map(photo -> new PhotoUrlResponse(photo.getImageUrl()))
                .toList();

        return new GetAllPhotoUrlResponse(photoUrlResponses);
    }

    @Transactional(readOnly = true)
    public GetAllPhotoUrlResponse getAllPhotosByJourneyId(String email, Long journeyId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        Journey journey = journeyRepository.findById(journeyId).orElseThrow(JourneyNotFoundException::new);

        journey.getTeam().getMemberTeams()
                .stream()
                .filter(memberTeam -> memberTeam.getMember().getId().equals(member.getId()))
                .findAny()
                .orElseThrow(TeamAccessDeniedException::new);

        List<PhotoUrlResponse> photoUrlResponses = photoRepository.findByJourneyId(journey.getId())
                .stream()
                .map(photo -> new PhotoUrlResponse(photo.getImageUrl()))
                .toList();

        return new GetAllPhotoUrlResponse(photoUrlResponses);
    }

}
