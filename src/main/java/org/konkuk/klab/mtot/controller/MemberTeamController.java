package org.konkuk.klab.mtot.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.request.MemberTeamJoinRequest;
import org.konkuk.klab.mtot.dto.response.MemberTeamGetAllResponse;
import org.konkuk.klab.mtot.dto.response.MemberTeamJoinResponse;
import org.konkuk.klab.mtot.service.MemberTeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberTeamController {

    private final MemberTeamService memberTeamService;

    @GetMapping("/members/{memberId}/teams")
    public ResponseEntity<MemberTeamGetAllResponse> getAllTeamByMemberId(@PathVariable Long memberId){
        MemberTeamGetAllResponse response = memberTeamService.getMemberTeamsByMemberId(memberId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/teams/join")
    public ResponseEntity<MemberTeamJoinResponse> registerMemberToTeam(@RequestBody @Valid MemberTeamJoinRequest request){
        MemberTeamJoinResponse response = memberTeamService.registerMemberToTeam(request);
        return ResponseEntity.ok(response);
    }
}
