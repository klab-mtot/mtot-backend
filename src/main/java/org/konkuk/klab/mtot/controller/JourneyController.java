package org.konkuk.klab.mtot.controller;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.response.CreateJourneyResponse;
import org.konkuk.klab.mtot.service.JourneyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/journey")
@RestController
@RequiredArgsConstructor
public class JourneyController {
    private final JourneyService journeyService;
    @PostMapping("/members/{memberEmail}/journies/{journeyName}/teams/{teamId}")
    public ResponseEntity<CreateJourneyResponse> createJourney(@PathVariable("memberEmail") String memberEmail, @PathVariable("journeyName") String journeyName, @PathVariable("teamId") Long teamId){
        CreateJourneyResponse createJourneyResponse = journeyService.createJourney(memberEmail, journeyName, teamId);
        return ResponseEntity.ok(createJourneyResponse);
    }
}
