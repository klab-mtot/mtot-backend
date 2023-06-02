package org.konkuk.klab.mtot.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konkuk.klab.mtot.domain.Friendship;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.exception.AlreadyFriendException;
import org.konkuk.klab.mtot.exception.DuplicateFriendshipException;
import org.konkuk.klab.mtot.repository.FriendshipRepository;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

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
    @DisplayName("친구 신청을 성공적으로 진행한다.")
    public void friendshipTest(){
        // 회원가입1
        Member member1 = new Member("donghoony", "abc@mail.com");
        Member member2 = new Member("gwonpyo", "def@mail.com");
        Long member1Id = memberRepository.save(member1).getId();
        Long member2Id = memberRepository.save(member2).getId();

        // 친구 요청 보내기 (friendship 테이블에 해당 요청 기록)
        friendshipService.requestFriend(member1.getEmail(), member2.getEmail());

        // 요청 제대로 들어갔는지 확인
        Optional<Friendship> friendship = friendshipRepository.findByRequesterIdAndReceiverId(member1Id, member2Id);
        assertThat(friendship).isNotEmpty();
        assertThat(friendship.get().getRequester().getId()).isEqualTo(member1Id);
    }

    @Test
    @DisplayName("친구 신청을 성공적으로 수락한다.")
    public void friendshipAcceptTest(){
        Member member1 = new Member("donghoony", "abc@mail.com");
        Member member2 = new Member("gwonpyo", "def@mail.com");
        Long member1Id = memberRepository.save(member1).getId();
        Long member2Id = memberRepository.save(member2).getId();
        
        // 친구 요청
        Long friendshipId = friendshipService.requestFriend(member1.getEmail(), member2.getEmail()).getId();

        // 요청 수락
        friendshipService.updateFriendship(member2.getEmail(), true, friendshipId);
        
        // DB에서 accept table false에서 true값으로 변경
        Optional<Friendship> friendship = friendshipRepository.findByRequesterIdAndReceiverId(member1Id, member2Id);
        assertThat(friendship).isNotEmpty();
        assertThat(friendship.get().isAccepted()).isTrue();
        assertThat(friendship.get().getReceiver().getId()).isEqualTo(member2Id);
    }

    @Test
    @DisplayName("친구 신청을 성공적으로 거절한다.")
    public void friendshipRefuseTest(){
        Member member1 = new Member("donghoony", "abc@mail.com");
        Member member2 = new Member("gwonpyo", "def@mail.com");
        Long member1Id = memberRepository.save(member1).getId();
        Long member2Id = memberRepository.save(member2).getId();

        // 친구 요청
        Long friendshipId = friendshipService.requestFriend(member1.getEmail(), member2.getEmail()).getId();

        // 요청 거절 (DB에서 요청 삭제)
        friendshipService.updateFriendship(member2.getEmail(), false, friendshipId);

        // 삭제되었는 확인
        Optional<Friendship> friendship = friendshipRepository.findByRequesterIdAndReceiverId(member1Id, member2Id);
        assertThat(friendship).isEmpty();
    }

    @Test
    @DisplayName("친구 신청 보낸 것 과 받은 것 (수락되지 않은 것만) 가져오기")
    public void friendshipCheckTest(){
        Member member1 = new Member("donghoony", "abc@mail.com");
        Member member2 = new Member("gwonpyo", "def@mail.com");
        Long member1Id = memberRepository.save(member1).getId();
        Long member2Id = memberRepository.save(member2).getId();

        // 친구 요청
        friendshipService.requestFriend(member1.getEmail(), member2.getEmail());

        // 친구 신청 온 것 확인
        assertThat(friendshipRepository.findPendingFriendshipReceivedByMemberId(member2Id)).hasSize(1);
        // 친구 신청 보낸 것 확인
        assertThat(friendshipRepository.findPendingFriendshipRequestedByMemberId(member1Id)).hasSize(1);
    }

    @Test
    @DisplayName("친구 목록을 성공적으로 가져온다.")
    public void friendCheckTest(){
        Member member1 = new Member("donghoony", "abc@mail.com");
        Member member2 = new Member("gwonpyo", "def@mail.com");
        Long member1Id = memberRepository.save(member1).getId();
        Long member2Id = memberRepository.save(member2).getId();

        Friendship friendship = new Friendship(member1, member2);
        friendship.setAccepted(true);
        friendshipRepository.save(friendship);

        assertThat(friendshipRepository.findFriendsByMemberId(member1Id)).hasSize(1);
        assertThat(friendshipRepository.findFriendsByMemberId(member2Id)).hasSize(1);
    }

    @Test
    @DisplayName("같은 친구 요청을 두 번 보냈을 때 예외를 발생한다.")
    public void duplicationCheckTest() {
        Member member1 = new Member("donghoony", "abc@mail.com");
        Member member2 = new Member("gwonpyo", "def@mail.com");
        Long member1Id = memberRepository.save(member1).getId();
        Long member2Id = memberRepository.save(member2).getId();

        // 친구 요청
        friendshipService.requestFriend(member1.getEmail(), member2.getEmail());

        // DuplicateFriendshipException 발생
        assertThatThrownBy(()->friendshipService.requestFriend(member1.getEmail(), member2.getEmail()))
                .isInstanceOf(DuplicateFriendshipException.class);
    }

    @Test
    @DisplayName("한 쪽에서 친구 신청을 보내고, 다른 쪽에서 거절한 뒤, 다른 쪽에서 친구를 성공적으로 신청한다.")
    public void rejectFriendAndSendAnotherFriendRequest(){
        Member member1 = new Member("donghoony", "abc@mail.com");
        Member member2 = new Member("gwonpyo", "def@mail.com");
        Long member1Id = memberRepository.save(member1).getId();
        Long member2Id = memberRepository.save(member2).getId();

        // 친구 요청
        Friendship friendship = new Friendship(member1, member2);
        Long friendshipId = friendshipRepository.save(friendship).getId();
        friendshipService.updateFriendship(member2.getEmail(), false, friendshipId);

        assertThatNoException().isThrownBy(()-> friendshipService.requestFriend(member2.getEmail(), member1.getEmail()));
    }

    @Test
    @DisplayName("친구 수락한 뒤 다시 친구신청을 보냈을 때 예외를 발생한다.")
    public void sendDuplicateFriendRequestAfterAccept(){
        Member member1 = new Member("donghoony", "abc@mail.com");
        Member member2 = new Member("gwonpyo", "def@mail.com");
        Long member1Id = memberRepository.save(member1).getId();
        Long member2Id = memberRepository.save(member2).getId();

        // 친구 요청
        Friendship friendship = new Friendship(member1, member2);
        Long friendshipId = friendshipRepository.save(friendship).getId();
        friendshipService.updateFriendship(member2.getEmail(), true, friendshipId);

        assertThatThrownBy(()-> friendshipService.requestFriend(member2.getEmail(), member1.getEmail()))
                .isInstanceOf(AlreadyFriendException.class);
    }

    @AfterEach
    public void tearDown(){
        friendshipRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
