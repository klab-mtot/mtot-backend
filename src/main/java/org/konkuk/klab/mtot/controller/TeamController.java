package org.konkuk.klab.mtot.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.request.TeamCreateRequest;
import org.konkuk.klab.mtot.dto.response.TeamCreateResponse;
import org.konkuk.klab.mtot.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;


    @PostMapping("/create")
    public ResponseEntity<TeamCreateResponse> createTeam(@RequestBody @Valid TeamCreateRequest request){
        TeamCreateResponse response = teamService.createTeam(request);
        return ResponseEntity.ok(response);
    }
}
