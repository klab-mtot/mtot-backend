package org.konkuk.klab.mtot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.MemberTeam;
import org.konkuk.klab.mtot.domain.Team;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.konkuk.klab.mtot.repository.MemberTeamRepository;
import org.konkuk.klab.mtot.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TeamServiceTest {

    @Autowired
    TeamService teamService;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberTeamRepository memberTeamRepository;

    @Test
    @DisplayName("그룹을 만들고, 그 멤버가 리더장이 된다.")
    public void createGroupTest() throws Exception{
        //given
        Member member = new Member("Lee", "abc@naver.com");
        Long id = memberRepository.save(member).getId();

        //when
        teamService.createTeam(member.getEmail(), "My_Group");

        //then
        List<Team> team = teamRepository.findAll();
        assertThat(team).hasSize(1);
        assertThat(team.get(0).getLeaderId()).isEqualTo(id);

        List<MemberTeam> allByMemberId = memberTeamRepository.findAllByMemberId(id);
        assertThat(allByMemberId).hasSize(1);
    }


    @AfterEach
    void tearDown(){
        memberTeamRepository.deleteAll();
        teamRepository.deleteAll();
        memberRepository.deleteAll();
    }
}