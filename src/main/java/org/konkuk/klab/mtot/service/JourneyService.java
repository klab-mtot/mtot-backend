package org.konkuk.klab.mtot.service;


import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.dto.request.JourneyCreateRequest;
import org.konkuk.klab.mtot.dto.request.MemberSignUpRequest;
import org.konkuk.klab.mtot.dto.response.JourneyCreateResponse;
import org.konkuk.klab.mtot.dto.response.MemberSignUpResponse;
import org.konkuk.klab.mtot.repository.JourneyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class JourneyService {
    private final JourneyRepository journeyRepository;
    // 저니 생성
    @Transactional
    public JourneyCreateResponse CreateJourney(JourneyCreateRequest request){
        Journey journey = new Journey(request.getName(), request.getTeam_id());
        Long journey_id = journeyRepository.save(journey).getId();
        return new JourneyCreateResponse(journey_id);
    }
    //저니 핀 목록 제공

    //저니 핀 목록 추가기능은 여기서 생성은 핀 서비스에서 처리

    //저니 사진 목록 추가만 여기서 사진 등록은 사진 서비스에서 처리

    //저니 포스트 자동으로 생성되는거 연결



}
