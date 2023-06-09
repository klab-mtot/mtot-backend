package org.konkuk.klab.mtot.controller;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.request.FriendshipAcceptRequest;
import org.konkuk.klab.mtot.dto.request.FriendshipRequest;
import org.konkuk.klab.mtot.dto.response.FriendshipUpdateResponse;
import org.konkuk.klab.mtot.dto.response.GetAllFriendshipInfoResponse;
import org.konkuk.klab.mtot.dto.response.GetAllPendingFriendshipResponse;
import org.konkuk.klab.mtot.dto.response.SendFriendshipResponse;
import org.konkuk.klab.mtot.oauth.LoginMemberEmail;
import org.konkuk.klab.mtot.service.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friendship")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @GetMapping
    public ResponseEntity<GetAllFriendshipInfoResponse> getFriendList(@LoginMemberEmail String email){
        GetAllFriendshipInfoResponse response = friendshipService.findFriendshipList(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SendFriendshipResponse> requestFriendship(@LoginMemberEmail String email,
                                                                    @RequestBody FriendshipRequest request){
        SendFriendshipResponse response = friendshipService.requestFriend(email, request.getReceiverEmail());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending")
    @ResponseBody
    public ResponseEntity<GetAllPendingFriendshipResponse> getPendingFriendship(@LoginMemberEmail String email){
        GetAllPendingFriendshipResponse response = friendshipService.getAllPendingFriendRequests(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reject")
    @ResponseBody
    public ResponseEntity<FriendshipUpdateResponse> rejectFriendList(@LoginMemberEmail String email,
                                                                     @RequestBody FriendshipAcceptRequest request){
        FriendshipUpdateResponse response = friendshipService.updateFriendship(email, false, request.getFriendshipId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/accept")
    @ResponseBody
    public ResponseEntity<FriendshipUpdateResponse> acceptFriendList(@LoginMemberEmail String email,
                                                                     @RequestBody FriendshipAcceptRequest request){
        FriendshipUpdateResponse response = friendshipService.updateFriendship(email, true, request.getFriendshipId());
        return ResponseEntity.ok(response);
    }
}
