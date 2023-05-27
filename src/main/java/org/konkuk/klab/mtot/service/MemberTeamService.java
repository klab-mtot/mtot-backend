package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.MemberTeam;
import org.konkuk.klab.mtot.domain.Team;
import org.konkuk.klab.mtot.dto.response.MemberTeamGetAllResponse;
import org.konkuk.klab.mtot.dto.response.MemberTeamGetResponse;
import org.konkuk.klab.mtot.dto.response.MemberTeamJoinResponse;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.konkuk.klab.mtot.repository.MemberTeamRepository;
import org.konkuk.klab.mtot.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberTeamService {

    private final MemberTeamRepository memberTeamRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    @Transactional
    public MemberTeamJoinResponse registerMemberToTeam(String leaderEmail, Long teamId, Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new RuntimeException("ID에 해당하는 멤버를 찾을 수 없습니다."));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(()-> new RuntimeException("ID에 해당하는 그룹를 찾을 수 없습니다."));
        Member leader = memberRepository.findByEmail(leaderEmail)
                .orElseThrow(()-> new RuntimeException("로그인 유저를 찾을 수 없습니다."));
        if (!Objects.equals(team.getLeaderId(), leader.getId())) throw new RuntimeException("해당 유저는 리더가 아닙니다.");

        memberTeamRepository.findByMemberIdAndTeamId(teamId, memberId)
                .ifPresent(memberTeam -> {throw new RuntimeException("그룹에 이미 존재하는 멤버입니다.");});

        memberTeamRepository.save(new MemberTeam(member, team));
        return new MemberTeamJoinResponse(team.getId());
    }

    @Transactional(readOnly = true)
    public MemberTeamGetAllResponse getMemberTeamsByMemberEmail(String email){
        List<MemberTeam> memberTeams = memberTeamRepository.findAllByMemberEmail(email);
        return new MemberTeamGetAllResponse(memberTeams.stream()
                .map(memberTeam -> new MemberTeamGetResponse(memberTeam.getMember().getId(), memberTeam.getTeam().getId()))
                .toList());
    }

}
