package org.konkuk.klab.mtot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konkuk.klab.mtot.domain.*;
import org.konkuk.klab.mtot.dto.response.PinUpdateResponse;
import org.konkuk.klab.mtot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PinServiceTest {

    @Autowired
    private PinService pinService;
    @Autowired
    private PinRepository pinRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberTeamRepository memberTeamRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private JourneyRepository journeyRepository;


    Location gonghakLocation = new Location(37.541757, 127.078780);
    Location newYearLocation = new Location(37.543612, 127.077455);
    Location nocheonLocation = new Location(37.541510, 127.077796);
    // 새천년관 - 공학관 사이 거리 꽤 멀다

    private String name = "donghoony";
    private String email = "abc@mail.com";

    Member member;
    Team team;
    MemberTeam memberTeam;
    Journey journey;
    private Long memberId, teamId, memberTeamId, journeyId;
    private void register() {
        member = new Member(name, email);
        memberId = memberRepository.save(member).getId();

        team = new Team("My team", memberId);
        teamId = teamRepository.save(team).getId();

        memberTeam = new MemberTeam(member, team);
        memberTeamId = memberTeamRepository.save(memberTeam).getId();

        journey = new Journey(team, "My Journey");
        journeyId = journeyRepository.save(journey).getId();
    }

    @Test
    @DisplayName("핀을 성공적으로 생성한다.")
    public void createPin(){
        register();
        pinService.pinRequest(email, journeyId, gonghakLocation);

        List<Pin> found = pinRepository.findAll();
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getMember().getId()).isEqualTo(memberId);
    }

    @Test
    @DisplayName("사용자가 여정에서 가장 최근에 만든 핀을 가져온다.")
    public void getRecentPin() throws InterruptedException {
        register();
        Pin pin1 = new Pin(member, journey, gonghakLocation);
        sleep(100);
        Pin pin2 = new Pin(member, journey, newYearLocation);
        sleep(100);
        Pin pin3 = new Pin(member, journey, gonghakLocation);
        sleep(100);
        Member newmember = new Member("someone", "abc");
        memberRepository.save(newmember);

        Pin pin4 = new Pin(newmember, journey, newYearLocation);

        pinRepository.save(pin1);
        pinRepository.save(pin2);
        pinRepository.save(pin3);
        pinRepository.save(pin4);

        Long pin3Id = pinRepository.save(pin3).getId();

        Optional<Pin> found = pinRepository.findFirstPinByMemberIdAndJourneyId(memberId, journeyId);
        assertThat(found).isNotEmpty();
        assertThat(found.get().getId()).isEqualTo(pin3Id);
    }

    @Test
    @DisplayName("최근 핀보다 100m 보다 많이 떨어진 위치에서 핀을 요청하면 새로운 핀을 생성한다.")
    public void distinctPinTest(){
        register();
        pinService.pinRequest(email, journeyId, newYearLocation);
        pinService.pinRequest(email, journeyId, gonghakLocation);

        List<Pin> found = pinRepository.findAll();
        assertThat(found).hasSize(2);
    }

    @Test
    @DisplayName("최근 핀과 100m 이내 위치에서 핀을 요청하면 이전 핀을 사용한다.")
    public void indistinctPinTest(){
        register();
        pinService.pinRequest(email, journeyId, gonghakLocation);
        PinUpdateResponse pinUpdateResponse = pinService.pinRequest(email, journeyId, nocheonLocation);

        List<Pin> found = pinRepository.findAll();
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getId()).isEqualTo(pinUpdateResponse.getPinId());
    }

    @Test
    @DisplayName("journey의 pin들을 가져옵니다.")
    public void getAllPinFromJourney(){
        register();
        // 2개 추가
        pinService.pinRequest(email, journeyId, newYearLocation);
        pinService.pinRequest(email, journeyId, gonghakLocation);

        // 따라서 해당 저니의 pin을 들고오면 2여야 함.
        assertThat(pinService.GetAllPinFromJourney(email, journeyId).getPins()).hasSize(2);
    }

    @AfterEach
    public void tearDown(){
        pinRepository.deleteAll();
        journeyRepository.deleteAll();
        memberTeamRepository.deleteAll();
        teamRepository.deleteAll();
        memberRepository.deleteAll();
    }
}