package org.konkuk.klab.mtot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.MemberTeam;
import org.konkuk.klab.mtot.domain.Team;
import org.konkuk.klab.mtot.dto.response.MemberTeamJoinResponse;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.konkuk.klab.mtot.repository.MemberTeamRepository;
import org.konkuk.klab.mtot.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberTeamServiceTest {

    @Autowired
    MemberTeamService memberTeamService;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberTeamRepository memberTeamRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버를 그룹에 추가한다.")
    public void MemberTeamRegister() throws Exception{
        //given
        Member member = new Member("Lee", "abc@naver.com");
        Long memberId = memberRepository.save(member).getId();
        Team team = new Team("New team", memberId);
        Long teamId = teamRepository.save(team).getId();

        //when
        memberTeamService.registerMemberToTeam(member.getEmail(), teamId, memberId);

        //then
        List<MemberTeam> all = memberTeamRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getTeam().getId()).isEqualTo(teamId);
        assertThat(all.get(0).getMember().getId()).isEqualTo(memberId);

    }

    @Test
    @DisplayName("중복 그룹 추가를 방지한다.")
    public void preventDuplicateMemberRegisteringTeam(){
        // given
        Member member = new Member("Lee", "abc@naver.com");
        Long memberId = memberRepository.save(member).getId();
        Team team = new Team("New team", memberId);
        Long teamId = teamRepository.save(team).getId();
        MemberTeamJoinResponse response = memberTeamService.registerMemberToTeam(member.getEmail(), teamId, memberId);

        assertThat(memberTeamRepository.findAll()).hasSize(1);
        assertThat(memberTeamRepository.findAll().get(0).getTeam().getId()).isEqualTo(response.getGroupId());
        // when
        assertThatThrownBy(()->{
            memberTeamService.registerMemberToTeam(member.getEmail(), teamId, memberId);
        }).isInstanceOf(RuntimeException.class);
    }


    @Test
    @DisplayName("멤버 ID로 속한 팀 ID를 찾는다.")
    public void getMemberTeamsByMemberId(){
        // given
        Member member1 = new Member("Lee", "abc@naver.com");
        Long member1Id = memberRepository.save(member1).getId();

        Member member2 = new Member("Kim", "abcd@naver.com");
        Long member2Id = memberRepository.save(member2).getId();

        Team team1 = new Team("New team", member1Id);
        Team team2 = new Team("Team2", member1Id);
        teamRepository.save(team1);
        teamRepository.save(team2);

        memberTeamRepository.save(new MemberTeam(member1, team1));
        memberTeamRepository.save(new MemberTeam(member1, team2));
        memberTeamRepository.save(new MemberTeam(member2, team1));

        // team1: Lee, Kim, team2: Lee

        // when
        List<MemberTeam> allByMember1Id = memberTeamRepository.findAllByMemberId(member1Id);
        List<MemberTeam> allByMember2Id = memberTeamRepository.findAllByMemberId(member2Id);

        // then
        assertThat(allByMember1Id).hasSize(2);
        assertThat(allByMember2Id).hasSize(1);
    }


    @AfterEach
    void tearDown(){
        memberTeamRepository.deleteAll(); // 외래키 Delete 순서 고려해야 함
        teamRepository.deleteAll();
        memberRepository.deleteAll();
    }
}