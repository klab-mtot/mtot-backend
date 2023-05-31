package org.konkuk.klab.mtot.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.request.MemberTeamJoinRequest;
import org.konkuk.klab.mtot.dto.response.MemberTeamGetAllResponse;
import org.konkuk.klab.mtot.dto.response.MemberTeamJoinResponse;
import org.konkuk.klab.mtot.oauth.LoginMemberEmail;
import org.konkuk.klab.mtot.service.MemberTeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberTeamController {

    private final MemberTeamService memberTeamService;

    @GetMapping("/teams")
    public ResponseEntity<MemberTeamGetAllResponse> getAllTeamByMemberEmail(@LoginMemberEmail String email){
        MemberTeamGetAllResponse response = memberTeamService.getMemberTeamsByMemberEmail(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/teams/register")
    public ResponseEntity<MemberTeamJoinResponse> registerMemberToTeam(@LoginMemberEmail String email,
                                                                       @RequestBody @Valid MemberTeamJoinRequest request){
        MemberTeamJoinResponse response = memberTeamService.registerMemberToTeam(email, request.getTeamId(), request.getMemberId());
        return ResponseEntity.ok(response);
    }
}
