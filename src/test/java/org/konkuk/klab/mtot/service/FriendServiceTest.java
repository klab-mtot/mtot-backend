package org.konkuk.klab.mtot.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konkuk.klab.mtot.domain.Friendship;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.dto.request.FriendshipRequest;
import org.konkuk.klab.mtot.dto.request.FriendshipUpdateRequest;
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

        // DB에서 유저 가져오기
        Optional<Member> requester = memberRepository.findByEmail("abc@naver.com");
        Optional<Member> receiver = memberRepository.findByEmail("abcdef@naver.com");

        // 친구 요청 보내기 (friendship 테이블에 해당 요청 기록)
        FriendshipRequest friendshipRequest = new FriendshipRequest(requester.get().getEmail(), receiver.get().getEmail());
        friendshipService.requestFriend(friendshipRequest);

        // 요청 제대로 들어갔는지 확인
        Optional<Friendship> friendship = friendshipRepository.findByRequesterIdAndReceiverId(requester.get().getId(), receiver.get().getId());
        assertThat(friendship).isNotEmpty();
    }

    @Test
    @DisplayName("친구 신청 및 등록 이후 수락 진행")
    public void friendshipAcceptTest(){
        String mail = "LeeSJ@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest("LeeSJ", mail, "q1w2e3r4");
        memberService.join(request);

        mail = "abcdef@naver.com";
        request = new MemberSignUpRequest("gwonpyo", mail, "q1w2e3r4");
        memberService.join(request);

        Optional<Member> requester = memberRepository.findByEmail("LeeSJ@naver.com");
        Optional<Member> receiver = memberRepository.findByEmail("abcdef@naver.com");
        
        // 친구 요청
        FriendshipRequest friendshipRequest = new FriendshipRequest(requester.get().getEmail(), receiver.get().getEmail());
        friendshipService.requestFriend(friendshipRequest);
        
        // 요청 수락
        FriendshipUpdateRequest friendshipUpdateRequest = new FriendshipUpdateRequest(requester.get().getEmail(), receiver.get().getEmail(), true);
        friendshipService.updateFriendship(friendshipUpdateRequest);
        
        // DB에서 accept table false에서 true값으로 변경
        Optional<Friendship> friendship = friendshipRepository.findByRequesterIdAndReceiverId(requester.get().getId(), receiver.get().getId());
        assertThat(friendship.get().isAccept()).isTrue();
    }

    @Test
    @DisplayName("친구 신청 및 등록 이후 거절 진행")
    public void friendshipRefuseTest(){
        String mail = "LeeSJ@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest("KBC", mail, "q1w2e3r4");
        memberService.join(request);

        mail = "abcdef@naver.com";
        request = new MemberSignUpRequest("gwonpyo", mail, "q1w2e3r4");
        memberService.join(request);

        Optional<Member> requester = memberRepository.findByEmail("LeeSJ@naver.com");
        Optional<Member> receiver = memberRepository.findByEmail("abcdef@naver.com");

        // 친구 요청
        FriendshipRequest friendshipRequest = new FriendshipRequest(requester.get().getEmail(), receiver.get().getEmail());
        friendshipService.requestFriend(friendshipRequest);

        // 요청 거절
        FriendshipUpdateRequest friendshipUpdateRequest = new FriendshipUpdateRequest(requester.get().getEmail(), receiver.get().getEmail(), false);
        friendshipService.updateFriendship(friendshipUpdateRequest);
        
        // 해당 요청 DB에서 삭제
        Optional<Friendship> friendship = friendshipRepository.findByRequesterIdAndReceiverId(requester.get().getId(), receiver.get().getId());
        assertThat(friendship).isEmpty();
    }
}
