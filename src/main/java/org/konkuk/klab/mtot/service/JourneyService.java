package org.konkuk.klab.mtot.service;


import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Team;
import org.konkuk.klab.mtot.dto.response.JourneyCreateResponse;
import org.konkuk.klab.mtot.exception.TeamAccessDeniedException;
import org.konkuk.klab.mtot.exception.TeamNotFoundException;
import org.konkuk.klab.mtot.repository.JourneyRepository;
import org.konkuk.klab.mtot.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JourneyService {

    private final JourneyRepository journeyRepository;
    private final TeamRepository teamRepository;
    @Transactional
    public JourneyCreateResponse createJourney(String memberEmail, String journeyName, Long teamId){
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);

        team.getMemberTeams().stream().filter(memberTeam ->
                Objects.equals(memberTeam.getMember().getEmail(), memberEmail)
        ).findAny().orElseThrow(TeamAccessDeniedException::new);

        Journey journey = new Journey(team, journeyName);
        Long journeyId = journeyRepository.save(journey).getId();

        return new JourneyCreateResponse(journeyId);
    }
    //저니 핀 목록 제공

    //저니 핀 목록 추가기능은 여기서 생성은 핀 서비스에서 처리

    //저니 사진 목록 추가만 여기서 사진 등록은 사진 서비스에서 처리

    //저니 포스트 자동으로 생성되는거 연결



}
