package org.konkuk.klab.mtot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.MemberTeam;
import org.konkuk.klab.mtot.domain.Team;
import org.konkuk.klab.mtot.dto.response.CreateJourneyResponse;
import org.konkuk.klab.mtot.dto.response.GetJourneyListResponse;
import org.konkuk.klab.mtot.dto.response.GetJourneyResponse;
import org.konkuk.klab.mtot.exception.TeamAccessDeniedException;
import org.konkuk.klab.mtot.exception.TeamNotFoundException;
import org.konkuk.klab.mtot.repository.JourneyRepository;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.konkuk.klab.mtot.repository.MemberTeamRepository;
import org.konkuk.klab.mtot.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class JourneyServiceTest {

    @Autowired
    private JourneyService journeyService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JourneyRepository journeyRepository;
    @Autowired
    private MemberTeamRepository memberTeamRepository;
    @Autowired
    private TeamRepository teamRepository;


    @Test
    @DisplayName("여정을 새로 등록한다.")
    public void makeNewJourney(){
        // given
        Long teamId = registerAndReturnTeamId();

        // when
        journeyService.createJourney(email, journeyName, teamId);

        // then
        List<Journey> all = journeyRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo(journeyName);
    }

    @Test
    @DisplayName("여정을 조회한다.")
    public void checkValidJourney(){
        // given
        Map<String, Object> map = registerAndReturnMap();
        String email = (String) map.get("email");
        Long teamId = (Long) map.get("teamId");

        // when
        CreateJourneyResponse createJourneyResponse = journeyService.createJourney(email, journeyName, teamId);
        Long journeyId = createJourneyResponse.getId();

        // then
        GetJourneyResponse getJourneyResponse = journeyService.getJourney(email, journeyId);
        assertThat(getJourneyResponse.getJourneyId()).isEqualTo(journeyId);
        assertThat(getJourneyResponse.getName()).isEqualTo(journeyName);
    }

    @Test
    @DisplayName("특정 멤버의 여정 리스트를 조회한다.")
    public void checkValidJourneyList(){
        // given
        Map<String, Object> map = registerAndReturnMap();
        String email = (String) map.get("email");
        Long teamId = (Long) map.get("teamId");

        // when
        CreateJourneyResponse createJourneyResponse1 = journeyService.createJourney(email, "My Journey 1", teamId);
        CreateJourneyResponse createJourneyResponse2 = journeyService.createJourney(email, "My Journey 2", teamId);
        CreateJourneyResponse createJourneyResponse3 = journeyService.createJourney(email, "My Journey 3", teamId);
        List<CreateJourneyResponse> createJourneyResponses = List.of(createJourneyResponse1, createJourneyResponse2, createJourneyResponse3);


        // then
        GetJourneyListResponse getJourneyListResponse = journeyService.getJourneyList(email);
        List<GetJourneyResponse> journeys = getJourneyListResponse.getJourneys();
        List<Long> journeyIds = journeys.stream().map(journey -> journey.getJourneyId()).toList();
        IntStream.range(0, 3).mapToObj(i -> assertThat(journeyIds.get(i).equals(createJourneyResponses.get(i))));
    }

    @Test
    @DisplayName("존재하지 않는 그룹의 여정 등록 시 예외를 발생한다.")
    public void checkValidGroupId(){
        // given
        Long teamId = registerAndReturnTeamId();

        // when
        assertThatThrownBy(()-> journeyService.createJourney(email, journeyName, teamId + 1))
                .isInstanceOf(TeamNotFoundException.class);
    }

    @Test
    @DisplayName("그룹에 속하지 않는 멤버가 여정을 추가할 시 예외를 발생한다.")
    public void checkValidGroupMember(){
        // given
        Long teamId = registerAndReturnTeamId();
        String newMail = "def@mail.com";
        Member member = new Member("Park", newMail);
        Long memberId = memberRepository.save(member).getId();

        // when
        assertThatThrownBy(()->journeyService.createJourney(newMail, journeyName, teamId))
                .isInstanceOf(TeamAccessDeniedException.class);

    }

    private final String email = "abc@mail.com";
    private final String journeyName = "My Journey";
    private Long registerAndReturnTeamId(){
        Member member = new Member("Lee", email);
        Long id = memberRepository.save(member).getId();
        Team team = new Team("My Team", id);
        Long teamId = teamRepository.save(team).getId();
        MemberTeam memberTeam = new MemberTeam(member, team);
        memberTeamRepository.save(memberTeam);
        return teamId;
    }

    private Map<String, Object> registerAndReturnMap(){
        Member member = new Member("Lee", email);
        Long memberId = memberRepository.save(member).getId();
        Team team = new Team("My Team", memberId);
        Long teamId = teamRepository.save(team).getId();
        MemberTeam memberTeam = new MemberTeam(member, team);
        memberTeamRepository.save(memberTeam);

        Map<String, Object> map = Map.ofEntries(
                Map.entry("email", email),
                Map.entry("teamId", teamId),
                Map.entry("memberId", memberId)
        );

        return map;
    }

    @AfterEach
    public void tearDown(){
        journeyRepository.deleteAll();
        memberTeamRepository.deleteAll();
        teamRepository.deleteAll();
        memberRepository.deleteAll();
    }
}