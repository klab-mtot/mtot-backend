package org.konkuk.klab.mtot.controller;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.request.FriendshipCheckRequest;
import org.konkuk.klab.mtot.dto.request.FriendshipRequest;
import org.konkuk.klab.mtot.dto.request.FriendshipAcceptRequest;
import org.konkuk.klab.mtot.dto.response.*;
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

    @GetMapping("/friendship/pending")
    @ResponseBody
    public ResponseEntity<GetAllPendingFriendshipResponse> getPendingFriendship(@LoginMemberEmail String email,
                                                                        @RequestBody FriendshipCheckRequest request){
        GetAllPendingFriendshipResponse response = friendshipService.getAllPendingFriendRequests(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/friendship/reject")
    @ResponseBody
    public ResponseEntity<FriendshipUpdateResponse> rejectFriendList(@LoginMemberEmail String email,
                                                                     @RequestBody FriendshipAcceptRequest request){
        FriendshipUpdateResponse response = friendshipService.updateFriendship(email, false, request.getFriendShipId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/friendship/accept")
    @ResponseBody
    public ResponseEntity<FriendshipUpdateResponse> acceptFriendList(@LoginMemberEmail String email,
                                                                     @RequestBody FriendshipAcceptRequest request){
        FriendshipUpdateResponse response = friendshipService.updateFriendship(email, true, request.getFriendShipId());
        return ResponseEntity.ok(response);
    }
}
