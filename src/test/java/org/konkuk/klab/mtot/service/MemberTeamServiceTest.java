package org.konkuk.klab.mtot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.MemberTeam;
import org.konkuk.klab.mtot.domain.Team;
import org.konkuk.klab.mtot.dto.response.MemberTeamJoinResponse;
import org.konkuk.klab.mtot.exception.DuplicateMemberOnTeamException;
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
        memberTeamService.registerMemberToTeam(member.getEmail(), teamId, member.getEmail());

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
        MemberTeamJoinResponse response = memberTeamService.registerMemberToTeam(member.getEmail(), teamId, member.getEmail());

        assertThat(memberTeamRepository.findAll()).hasSize(1);
        assertThat(memberTeamRepository.findAll().get(0).getTeam().getId()).isEqualTo(response.getTeamId());
        // when
        assertThatThrownBy(()-> memberTeamService.registerMemberToTeam(member.getEmail(), teamId, member.getEmail())).isInstanceOf(DuplicateMemberOnTeamException.class);
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

    @Test
    @DisplayName("멤버를 그룹에 추가한다.")
    public void getCorrectTeamIdTest(){
        //given (멤버 등록)
        Member member1 = new Member("Lee", "abc@naver.com");
        Member member2 = new Member("Kim", "abcd@naver.com");
        Member member3 = new Member("Nam", "abcde@naver.com");

        Long memberId1 = memberRepository.save(member1).getId();
        Long memberId2 = memberRepository.save(member2).getId();
        Long memberId3 = memberRepository.save(member3).getId();

        // member1이 팀을 만듬
        Team team1 = new Team("team1", memberId1);
        Long teamId1 = teamRepository.save(team1).getId();
        memberTeamRepository.save(new MemberTeam(member1, team1));

        // member1이 팀에 인원을 추가함
        memberTeamService.registerMemberToTeam(member1.getEmail(), teamId1, memberId2);
        memberTeamService.registerMemberToTeam(member1.getEmail(), teamId1, memberId3);

        // member3가 팀을 만듬
        Team team2 = new Team("team2", memberId3);
        teamRepository.save(team2);
        memberTeamRepository.save(new MemberTeam(member3, team2)).getId();

        // member3와 member1이 만든 팀의 id의 차이는 1이어야 함.
        Long team1Id = memberTeamService.getMemberTeamsByMemberEmail(member3.getEmail()).getTeamList().get(0).getTeamId();
        Long team2Id = memberTeamService.getMemberTeamsByMemberEmail(member3.getEmail()).getTeamList().get(1).getTeamId();
        assertThat(team2Id-team1Id).isEqualTo(1);
    }


    @AfterEach
    void tearDown(){
        memberTeamRepository.deleteAll(); // 외래키 Delete 순서 고려해야 함
        teamRepository.deleteAll();
        memberRepository.deleteAll();
    }
}