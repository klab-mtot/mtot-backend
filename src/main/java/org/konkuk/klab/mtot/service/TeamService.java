package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.Team;
import org.konkuk.klab.mtot.dto.response.TeamCreateResponse;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.konkuk.klab.mtot.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final MemberTeamService memberTeamService;

    @Transactional
    public TeamCreateResponse createTeam(String email, String teamName){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("회원이 존재하지 않습니다."));

        Team team = new Team(teamName, member.getId());
        Long teamId = teamRepository.save(team).getId();
        memberTeamService.registerMemberToTeam(member.getEmail(), teamId, member.getEmail());
        return new TeamCreateResponse(teamId);
    }
}
