package org.konkuk.klab.mtot.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konkuk.klab.mtot.domain.Friendship;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.dto.request.FriendshipRequest;
import org.konkuk.klab.mtot.dto.request.MemberSignUpRequest;
import org.konkuk.klab.mtot.repository.FriendshipRepository;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FriendServiceTest {
    @Autowired
    FriendshipService friendshipService;
    @Autowired
    FriendshipRepository friendshipRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("친구 신청 및 등록을 진행한다.")
    public void friendshipTest(){
        String mail = "abc@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest("donghoony", mail, "q1w2e3r4");
        memberService.join(request);

        mail = "abcdef@naver.com";
        request = new MemberSignUpRequest("gwonpyo", mail, "q1w2e3r4");
        memberService.join(request);

        Optional<Member> requester = memberRepository.findByEmail("abc@naver.com");
        Optional<Member> receiver = memberRepository.findByEmail("abcdef@naver.com");

        FriendshipRequest friendshipRequest = new FriendshipRequest(requester.get().getEmail(), receiver.get().getEmail());
        friendshipService.requestFriend(friendshipRequest);

        Optional<Friendship> friendship = friendshipRepository.findByRequesterIdAndReceiverId(requester.get().getId(), receiver.get().getId());
        assertThat(friendship).isNotEmpty();
    }
}
