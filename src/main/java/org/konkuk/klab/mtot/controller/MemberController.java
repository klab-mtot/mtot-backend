package org.konkuk.klab.mtot.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.request.MemberSignUpRequest;
import org.konkuk.klab.mtot.dto.response.MemberGetAllResponse;
import org.konkuk.klab.mtot.dto.response.MemberSignUpResponse;
import org.konkuk.klab.mtot.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/all")
    public ResponseEntity<MemberGetAllResponse> getAllMember(){
        MemberGetAllResponse response = memberService.getAllMembers();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    public ResponseEntity<MemberSignUpResponse> signUp(@RequestBody @Valid MemberSignUpRequest request){
        MemberSignUpResponse response = memberService.join(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public Map<String, Object> currentUser(OAuth2AuthenticationToken oAuth2AuthenticationToken){
        return oAuth2AuthenticationToken.getPrincipal().getAttributes();
    }

}
