package org.konkuk.klab.mtot.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konkuk.klab.mtot.domain.Friendship;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.dto.request.*;
import org.konkuk.klab.mtot.repository.FriendshipRepository;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
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
        // 회원가입1
        String mail = "abc@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest("donghoony", mail, "q1w2e3r4");
        memberService.join(request);

        // 회원가입2
        mail = "abcdef@naver.com";
        request = new MemberSignUpRequest("gwonpyo", mail, "q1w2e3r4");
        memberService.join(request);

        // DB에서 유저 가져오기
        Optional<Member> requester = memberRepository.findByEmail("abc@naver.com");
        Optional<Member> receiver = memberRepository.findByEmail("abcdef@naver.com");

        // 친구 요청 보내기 (friendship 테이블에 해당 요청 기록)
        friendshipService.requestFriend(requester.get().getEmail(), receiver.get().getEmail());

        // 요청 제대로 들어갔는지 확인
        Optional<Friendship> friendship = friendshipRepository.findByRequesterIdAndReceiverId(requester.get().getId(), receiver.get().getId());
        assertThat(friendship).isNotEmpty();
    }

    @Test
    @DisplayName("친구 신청 및 등록 이후 수락 진행")
    public void friendshipAcceptTest(){
        // 회원가입1
        String mail = "LeeSJ@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest("LeeSJ", mail, "q1w2e3r4");
        memberService.join(request);

        // 회원가입2
        mail = "abcdef@naver.com";
        request = new MemberSignUpRequest("gwonpyo", mail, "q1w2e3r4");
        memberService.join(request);

        Optional<Member> requester = memberRepository.findByEmail("LeeSJ@naver.com");
        Optional<Member> receiver = memberRepository.findByEmail("abcdef@naver.com");
        
        // 친구 요청
        friendshipService.requestFriend(requester.get().getEmail(), receiver.get().getEmail());
        
        // 요청 수락
        friendshipService.updateFriendship(true, requester.get().getEmail(), receiver.get().getEmail());
        
        // DB에서 accept table false에서 true값으로 변경
        Optional<Friendship> friendship = friendshipRepository.findByRequesterIdAndReceiverId(requester.get().getId(), receiver.get().getId());
        assertThat(friendship.get().isAccepted()).isTrue();
    }

    @Test
    @DisplayName("친구 신청 및 등록 이후 거절 진행")
    public void friendshipRefuseTest(){
        // 회원가입1
        String mail = "LeeSJ@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest("KBC", mail, "q1w2e3r4");
        memberService.join(request);

        // 회원가입2
        mail = "abcdef@naver.com";
        request = new MemberSignUpRequest("gwonpyo", mail, "q1w2e3r4");
        memberService.join(request);

        Optional<Member> requester = memberRepository.findByEmail("LeeSJ@naver.com");
        Optional<Member> receiver = memberRepository.findByEmail("abcdef@naver.com");

        // 친구 요청
        FriendshipRequest friendshipRequest = new FriendshipRequest(requester.get().getEmail(), receiver.get().getEmail());
        friendshipService.requestFriend(requester.get().getEmail(), receiver.get().getEmail());

        // 요청 거절 (DB에서 요청 삭제)
        friendshipService.updateFriendship(false, requester.get().getEmail(), receiver.get().getEmail());

        // 삭제되었는 확인
        Optional<Friendship> friendship = friendshipRepository.findByRequesterIdAndReceiverId(requester.get().getId(), receiver.get().getId());
        assertThat(friendship).isEmpty();
    }

    @Test
    @DisplayName("친구 신청 보낸 것 과 받은 것 (수락되지 않은 것만) 가져오기")
    public void friendshipCheckTest(){
        // 회원가입1
        String mail = "LeeSJ@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest("KBC", mail, "q1w2e3r4");
        memberService.join(request);

        // 회원가입2
        mail = "abcdef@naver.com";
        request = new MemberSignUpRequest("gwonpyo", mail, "q1w2e3r4");
        memberService.join(request);

        Optional<Member> requester = memberRepository.findByEmail("LeeSJ@naver.com");
        Optional<Member> receiver = memberRepository.findByEmail("abcdef@naver.com");

        // 친구 요청
        friendshipService.requestFriend(requester.get().getEmail(), receiver.get().getEmail());

        // 친구 신청 온 것 확인
        List<Friendship> checkList1 = friendshipService.checkMemberReceiveNotAccept(requester.get().getEmail());
        assertThat(checkList1.size()==1).isTrue();
        // 친구 신청 보낸 것 확인
        List<Friendship> checkList2 = friendshipService.checkMemberRequestNotAccepted(receiver.get().getEmail());
        assertThat(checkList2.size()==1).isTrue();
    }

    @Test
    @DisplayName("친구 목록 가져오기")
    public void friendCheckTest(){
        // 회원가입1
        String mail = "LeeSJ@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest("KBC", mail, "q1w2e3r4");
        memberService.join(request);

        // 회원가입2
        mail = "abcdef@naver.com";
        request = new MemberSignUpRequest("gwonpyo", mail, "q1w2e3r4");
        memberService.join(request);

        Optional<Member> requester = memberRepository.findByEmail("LeeSJ@naver.com");
        Optional<Member> receiver = memberRepository.findByEmail("abcdef@naver.com");

        // 친구 요청
        friendshipService.requestFriend(requester.get().getEmail(), receiver.get().getEmail());

        // 요청 수락
        friendshipService.updateFriendship(true, requester.get().getEmail(), receiver.get().getEmail());

        // 친구 확인
        List<Friendship> checkList = friendshipService.findFriendshipList(requester.get().getEmail());
        assertThat(checkList.size()==1).isTrue();
    }

    @Test
    @DisplayName("같은 친구 요청 보내기")
    public void duplicationCheckTest() {
        // 회원가입1
        String mail = "LeeSJ@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest("KBC", mail, "q1w2e3r4");
        memberService.join(request);

        // 회원가입2
        mail = "abcdef@naver.com";
        request = new MemberSignUpRequest("gwonpyo", mail, "q1w2e3r4");
        memberService.join(request);

        Optional<Member> requester = memberRepository.findByEmail("LeeSJ@naver.com");
        Optional<Member> receiver = memberRepository.findByEmail("abcdef@naver.com");

        // 요청 여러 번 보냄
        friendshipService.requestFriend(requester.get().getEmail(), receiver.get().getEmail());

        // DuplicateFriendshipException 발생
        friendshipService.requestFriend(requester.get().getEmail(), receiver.get().getEmail());
    }
}
