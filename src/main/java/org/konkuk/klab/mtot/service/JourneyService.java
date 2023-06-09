package org.konkuk.klab.mtot.service;


import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.Post;
import org.konkuk.klab.mtot.domain.Team;
import org.konkuk.klab.mtot.dto.response.*;
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
               memberTeam.getMember().getEmail().equals(memberEmail)
        ).findAny().orElseThrow(TeamAccessDeniedException::new);

        Journey journey = new Journey(team, journeyName);
        Long journeyId = journeyRepository.save(journey).getId();

        return new CreateJourneyResponse(journeyId);
    }

    @Transactional(readOnly = true)
    public GetJourneyResponse getJourney(String memberEmail, Long journeyId){
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

        Journey journey = journeyRepository.findById(journeyId).orElseThrow(JourneyNotFoundException::new);
        Team team = journey.getTeam();


        team.getMemberTeams().stream()
                .filter(memberTeam -> memberTeam.getMember().getId().equals(member.getId()))
                .findAny()
                .orElseThrow(TeamAccessDeniedException::new);

        PostInfoResponse postInfoResponse = null;
        Post post = journey.getPost();
        if (post != null) postInfoResponse = new PostInfoResponse(journey.getPost().getId(), journey.getPost().getTitle(), journey.getPost().getArticle());

        return new GetJourneyResponse(
                journey.getId(),
                journey.getName(),
                postInfoResponse,
                journey.getPins()
                        .stream()
                        .map(pin -> {return new PinInfoResponse(pin.getId(), pin.getLocation());})
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public GetJourneyListResponse getJourneyList(String memberEmail){
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

        List<GetJourneyResponse> getJourneyResponses = journeyRepository.getJourneysFromMember(member.getId())
                .stream()
                .map(journey -> {
                    PostInfoResponse postInfoResponse = null;
                    Post post = journey.getPost();
                    if (post != null) postInfoResponse = new PostInfoResponse(journey.getPost().getId(), journey.getPost().getTitle(), journey.getPost().getArticle());

                    return new GetJourneyResponse(
                        journey.getId(),
                        journey.getName(),
                        postInfoResponse,
                        journey.getPins().
                                stream()
                                .map(pin -> {return new PinInfoResponse(pin.getId(), pin.getLocation());})
                                .toList()
                        );}
                )
                .toList();

        return new GetJourneyListResponse(getJourneyResponses);
    }
}
