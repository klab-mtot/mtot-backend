package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Friendship;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.dto.response.FriendshipResponse;
import org.konkuk.klab.mtot.exception.AlreadyFriendException;
import org.konkuk.klab.mtot.exception.DuplicateFriendshipException;
import org.konkuk.klab.mtot.exception.FriendshipNotFoundException;
import org.konkuk.klab.mtot.exception.MemberNotFoundException;
import org.konkuk.klab.mtot.repository.FriendshipRepository;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;

    @Transactional
    // 친구 요청 보내기 (친구 요청 DB에 저장)
    public FriendshipResponse requestFriend(String requesterEmail, String receiverEmail){
        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findByEmail(receiverEmail)
                .orElseThrow(MemberNotFoundException::new);
        validateDuplicateFriendship(requester, receiver);

        Friendship friendship = new Friendship(requester, receiver);
        return new FriendshipResponse(friendshipRepository.save(friendship).getId());
    }

    @Transactional(readOnly = true)
    // 유저 친구 확인
    public List<Friendship> findFriendshipList(String memberEmail){
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(MemberNotFoundException::new);
        return friendshipRepository.findFriendsByMemberId(member.getId());
    }

    @Transactional(readOnly = true)
    // 유저가 보낸 친구 요청 확인 (수락 되지 않은 것)
    public List<Friendship> checkMemberRequestNotAccepted(String memberEmail){
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(MemberNotFoundException::new);
        return friendshipRepository.findPendingFriendshipReceivedByMemberId(member.getId());
    }

    @Transactional(readOnly = true)
    // 유저가 받은 친구 요청 확인 (수락 하지 않은 것)
    public List<Friendship> checkMemberReceiveNotAccept(String memberEmail){
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(MemberNotFoundException::new);
        return friendshipRepository.findPendingFriendshipRequestedByMemberId(member.getId());
    }

    @Transactional
    // 친구 추가 거절 or 수락 요청 처리 및 이후 친구 삭제 기능 시 사용 가능
    public int updateFriendship(boolean isAccepted, String requesterEmail, String receiverEmail){
        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findByEmail(receiverEmail)
                .orElseThrow(MemberNotFoundException::new);

        Friendship friendship = friendshipRepository.findByRequesterIdAndReceiverId(requester.getId(), receiver.getId())
                .orElseThrow(FriendshipNotFoundException::new);

        int updatedRows; // 변환된 행 개수 반환 받음
        if(isAccepted){
            friendship.setAccepted(true);
            updatedRows = 1;
        } else updatedRows = friendshipRepository.deleteByRequesterIdAndReceiverId(requester.getId(), receiver.getId());

        return updatedRows;
    }

    // 친구 추가 요청시 중복된 friendship이 이미 존재하는지 확인
    private void validateDuplicateFriendship(Member requester, Member receiver){
        friendshipRepository.findFriendsByMemberId(requester.getId())
                        .stream()
                        .filter(f-> f.getReceiver().getId().equals(receiver.getId()))
                        .findAny()
                        .ifPresent(f->{throw new AlreadyFriendException();});

        friendshipRepository.findFriendsByMemberId(receiver.getId())
                .stream()
                .filter(f-> f.getReceiver().getId().equals(requester.getId()))
                .findAny()
                .ifPresent(f->{throw new AlreadyFriendException();});

        friendshipRepository.findPendingFriendshipRequestedByMemberId(requester.getId())
                .stream()
                .findAny()
                .ifPresent(friendship -> {
                    throw new DuplicateFriendshipException();
                });

        friendshipRepository.findPendingFriendshipReceivedByMemberId(receiver.getId())
                .stream()
                .findAny()
                .ifPresent(friendship -> {
                    throw new DuplicateFriendshipException();
                });
    }
}
