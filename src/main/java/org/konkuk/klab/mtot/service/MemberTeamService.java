package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.MemberTeam;
import org.konkuk.klab.mtot.domain.Team;
import org.konkuk.klab.mtot.dto.response.*;
import org.konkuk.klab.mtot.exception.*;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.konkuk.klab.mtot.repository.MemberTeamRepository;
import org.konkuk.klab.mtot.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberTeamService {

    private final MemberTeamRepository memberTeamRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    @Transactional
    public MemberTeamJoinResponse registerMemberToTeam(String leaderMail, Long teamId, String memberEmail){

        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(MemberNotFoundException::new);
        Member leader = memberRepository.findByEmail(leaderMail)
                .orElseThrow(MemberNotFoundException::new);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        memberTeamRepository.findByMemberEmailAndTeamId(memberEmail, teamId)
                .ifPresent(memberTeam -> {
                    throw new DuplicateMemberOnTeamException();
                });
        if (!team.getLeaderId().equals(leader.getId()))
            throw new NotALeaderException();

        memberTeamRepository.save(new MemberTeam(member, team));
        return new MemberTeamJoinResponse(team.getId());
    }

    @Transactional(readOnly = true)
    public MemberTeamGetAllResponse getMemberTeamsByMemberEmail(String email){
        List<MemberTeam> memberTeams = memberTeamRepository.findAllByMemberEmail(email);
        return new MemberTeamGetAllResponse(memberTeams.size(),
                memberTeams
                        .stream()
                        .map(memberTeam -> new MemberTeamGetResponse(memberTeam.getTeam().getId(), memberTeam.getTeam().getName()))
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public GetAllTeamMemberResponse getAllTeamMemberByTeamId(String email, Long teamId){
        memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);

        team.getMemberTeams().stream().filter(memberTeam ->
                memberTeam.getMember().getEmail().equals(email)
        ).findAny().orElseThrow(TeamAccessDeniedException::new);

        List<Member> members = memberRepository.getAllTeamMemberByTeamId(teamId);
        return new GetAllTeamMemberResponse(members.size(),
                members
                        .stream()
                        .map(member -> new GetTeamMemberResponse(member.getId(), member.getName(), member.getEmail()))
                        .toList()
        );
    }

}
