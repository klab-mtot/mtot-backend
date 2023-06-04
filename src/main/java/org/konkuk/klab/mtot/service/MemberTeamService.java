package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.MemberTeam;
import org.konkuk.klab.mtot.domain.Team;
import org.konkuk.klab.mtot.dto.response.MemberTeamGetAllResponse;
import org.konkuk.klab.mtot.dto.response.MemberTeamGetResponse;
import org.konkuk.klab.mtot.dto.response.MemberTeamJoinResponse;
import org.konkuk.klab.mtot.exception.DuplicateMemberOnTeamException;
import org.konkuk.klab.mtot.exception.MemberNotFoundException;
import org.konkuk.klab.mtot.exception.NotALeaderException;
import org.konkuk.klab.mtot.exception.TeamNotFoundException;
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
                        .map(memberTeam -> new MemberTeamGetResponse(memberTeam.getId(), memberTeam.getTeam().getName()))
                        .toList()
        );
    }

}
