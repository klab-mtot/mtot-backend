package org.konkuk.klab.mtot.controller;

import org.konkuk.klab.mtot.dto.request.FriendCheckRequest;
import org.konkuk.klab.mtot.dto.request.FriendshipCheckRequest;
import org.konkuk.klab.mtot.dto.request.FriendshipRequest;
import org.konkuk.klab.mtot.dto.request.FriendshipUpdateRequest;
import org.konkuk.klab.mtot.dto.response.FriendCheckResponse;
import org.konkuk.klab.mtot.dto.response.FriendshipCheckResponse;
import org.konkuk.klab.mtot.dto.response.FriendshipResponse;
import org.konkuk.klab.mtot.dto.response.FriendshipUpdateResponse;
import org.konkuk.klab.mtot.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class FriendshipController {
    @Autowired
    FriendshipService friendshipService;

    @GetMapping("/friendship")
    @ResponseBody
    public FriendCheckResponse getFriendList(@RequestBody FriendCheckRequest request){
        return new FriendCheckResponse(friendshipService.findFriendshipList(request.getMemberEmail()));
    }

    @PostMapping("/friendship")
    @ResponseBody
    public FriendshipResponse requestFriendship(@RequestBody FriendshipRequest request){
        return friendshipService.requestFriend(request.getRequesterEmail(), request.getReceiverEmail());
    }

    @GetMapping("/friendship/pending")
    @ResponseBody
    public FriendshipCheckResponse getPendingFriendship(@RequestBody FriendshipCheckRequest request){
        return new FriendshipCheckResponse(friendshipService.checkMemberReceiveNotAccept(request.getMemberEmail()), friendshipService.checkMemberRequestNotAccepted(request.getMemberEmail()));
    }

    @PostMapping("/friendship/reject")
    @ResponseBody
    public FriendshipUpdateResponse rejectFriendList(@RequestBody FriendshipUpdateRequest request){
        return new FriendshipUpdateResponse(friendshipService.updateFriendship(false, request.getRequesterEmail(), request.getReceiverEmail()));
    }

    @PostMapping("/friendship/accept")
    @ResponseBody
    public FriendshipUpdateResponse acceptFriendList(@RequestBody FriendshipUpdateRequest request){
        return new FriendshipUpdateResponse(friendshipService.updateFriendship(true, request.getRequesterEmail(), request.getReceiverEmail()));
    }
}
