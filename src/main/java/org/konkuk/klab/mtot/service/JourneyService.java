package org.konkuk.klab.mtot.service;


import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.Team;
import org.konkuk.klab.mtot.dto.response.CreateJourneyResponse;
import org.konkuk.klab.mtot.dto.response.GetJourneyListResponse;
import org.konkuk.klab.mtot.dto.response.GetJourneyResponse;
import org.konkuk.klab.mtot.exception.JourneyNotFoundException;
import org.konkuk.klab.mtot.exception.MemberNotFoundException;
import org.konkuk.klab.mtot.exception.TeamAccessDeniedException;
import org.konkuk.klab.mtot.exception.TeamNotFoundException;
import org.konkuk.klab.mtot.repository.JourneyRepository;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.konkuk.klab.mtot.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JourneyService {

    private final JourneyRepository journeyRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public CreateJourneyResponse createJourney(String memberEmail, String journeyName, Long teamId){
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);

        team.getMemberTeams().stream().filter(memberTeam ->
                Objects.equals(memberTeam.getMember().getEmail(), memberEmail)
        ).findAny().orElseThrow(TeamAccessDeniedException::new);

        Journey journey = new Journey(team, journeyName);
        Long journeyId = journeyRepository.save(journey).getId();

        return new CreateJourneyResponse(journeyId);
    }

    public GetJourneyResponse getJourney(String memberEmail, Long journeyId){
        Journey journey = journeyRepository.findById(journeyId).orElseThrow(JourneyNotFoundException::new);
        return new GetJourneyResponse(journey.getId(), journey.getName(), journey.getPost(), journey.getPins());
    }

    public GetJourneyListResponse getJourneyList(String memberEmail){
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
        List<GetJourneyResponse> getJourneyResponses = journeyRepository.getJourneysFromMember(member.getId()).stream().map(
                journey -> {
            return new GetJourneyResponse(journey.getId(),journey.getName(),journey.getPost(),journey.getPins());
        }).toList();

        return new GetJourneyListResponse(getJourneyResponses);
    }
    //저니 핀 목록 제공

    //저니 핀 목록 추가기능은 여기서 생성은 핀 서비스에서 처리

    //저니 사진 목록 추가만 여기서 사진 등록은 사진 서비스에서 처리

    //저니 포스트 자동으로 생성되는거 연결


}
