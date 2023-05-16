package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Team;
import org.konkuk.klab.mtot.dto.request.MemberTeamJoinRequest;
import org.konkuk.klab.mtot.dto.request.TeamCreateRequest;
import org.konkuk.klab.mtot.dto.response.TeamCreateResponse;
import org.konkuk.klab.mtot.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberTeamService memberTeamService;

    @Transactional
    public TeamCreateResponse createTeam(TeamCreateRequest request){
        Team team = new Team(request.getTeamName(), request.getMemberId());
        Long teamId = teamRepository.save(team).getId();
        memberTeamService.registerMemberToTeam(new MemberTeamJoinRequest(teamId, request.getMemberId()));
        return new TeamCreateResponse(teamId);
    }
}
