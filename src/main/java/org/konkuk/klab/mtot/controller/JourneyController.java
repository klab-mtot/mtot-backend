package org.konkuk.klab.mtot.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.request.CreateJourneyRequest;
import org.konkuk.klab.mtot.dto.response.CreateJourneyResponse;
import org.konkuk.klab.mtot.oauth.LoginMemberEmail;
import org.konkuk.klab.mtot.service.JourneyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/journey")
@RestController
@RequiredArgsConstructor
public class JourneyController {
    private final JourneyService journeyService;
    @PostMapping
    public ResponseEntity<CreateJourneyResponse> createJourney(@LoginMemberEmail String email, @RequestBody @Valid CreateJourneyRequest createJourneyRequest){
        CreateJourneyResponse createJourneyResponse = journeyService.createJourney(email, createJourneyRequest.getJourneyName(), createJourneyRequest.getTeamId());
        return ResponseEntity.ok(createJourneyResponse);
    }
}
