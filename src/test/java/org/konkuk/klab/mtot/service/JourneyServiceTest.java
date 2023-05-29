package org.konkuk.klab.mtot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.MemberTeam;
import org.konkuk.klab.mtot.domain.Team;
import org.konkuk.klab.mtot.repository.JourneyRepository;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.konkuk.klab.mtot.repository.MemberTeamRepository;
import org.konkuk.klab.mtot.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
    @DisplayName("존재하지 않는 그룹의 여정 등록 시 예외를 발생한다.")
    public void checkValidGroupId(){
        // given
        Long teamId = registerAndReturnTeamId();
        // when
        assertThatThrownBy(()-> journeyService.createJourney(email, journeyName, teamId + 1))
                .isInstanceOf(RuntimeException.class);
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
                .isInstanceOf(RuntimeException.class);

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

    @AfterEach
    public void tearDown(){
        journeyRepository.deleteAll();
        memberTeamRepository.deleteAll();
        teamRepository.deleteAll();
        memberRepository.deleteAll();
    }
}