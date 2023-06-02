package org.konkuk.klab.mtot.controller;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.request.PinUpdateRequest;
import org.konkuk.klab.mtot.dto.response.GetAllPinFromJourneyResponse;
import org.konkuk.klab.mtot.dto.response.PinUpdateResponse;
import org.konkuk.klab.mtot.oauth.LoginMemberEmail;
import org.konkuk.klab.mtot.service.PinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/journey")
public class PinController {

    private final PinService pinService;

    @PostMapping("/requestPin")
    public ResponseEntity<PinUpdateResponse> requestPin(@LoginMemberEmail String email,
                                                        @RequestBody PinUpdateRequest request){
        PinUpdateResponse response = pinService.pinRequest(email, request.getJourneyId(), request.getLocation());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{journeyId}/pin")
    public ResponseEntity<GetAllPinFromJourneyResponse> GetPinListFromJourney(
            @LoginMemberEmail String email,
            @PathVariable("journeyId") Long journeyId){
        GetAllPinFromJourneyResponse response = pinService.GetAllPinFromJourney(email, journeyId);
        return ResponseEntity.ok(response);
    }

}
