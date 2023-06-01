package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Friendship;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.dto.request.FriendCheckRequest;
import org.konkuk.klab.mtot.dto.request.FriendshipCheckRequest;
import org.konkuk.klab.mtot.dto.request.FriendshipRequest;
import org.konkuk.klab.mtot.dto.request.FriendshipUpdateRequest;
import org.konkuk.klab.mtot.dto.response.FriendshipResponse;
import org.konkuk.klab.mtot.exception.MemberNotFoundException;
import org.konkuk.klab.mtot.repository.FriendshipRepository;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;

    @Transactional
    // 친구 요청 보내기 (친구 요청 DB에 저장)
    public FriendshipResponse requestFriend(FriendshipRequest request){
        Member requester = memberRepository.findByEmail(request.getRequesterEmail())
                .orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findByEmail(request.getReceiverEmail())
                .orElseThrow(MemberNotFoundException::new);
        validateDuplicateFriendship(requester, receiver);

        Friendship friendship = new Friendship(requester, receiver);
        return new FriendshipResponse(friendshipRepository.save(friendship).getId());
    }

    @Transactional(readOnly = true)
    // 유저 친구 확인
    public List<Friendship> checkMemberFriend(FriendCheckRequest request){
        Member member = memberRepository.findByEmail(request.getMemberEmail()).orElseThrow(MemberNotFoundException::new);
        List<Friendship> friendshipList = friendshipRepository.findMemberFriend(member.getId());
        return friendshipList;
    }

    @Transactional(readOnly = true)
    // 유저가 보낸 친구 요청 확인 (수락 되지 않은 것)
    public List<Friendship> checkMemberRequestNotAccepted(FriendshipCheckRequest request){
        Member member = memberRepository.findByEmail(request.getMemberEmail()).orElseThrow(MemberNotFoundException::new);
        List<Friendship> friendshipList = friendshipRepository.findPendingFriendshipReceivedByMemberId(member.getId());
        return friendshipList;
    }

    @Transactional(readOnly = true)
    // 유저가 받은 친구 요청 확인 (수락 하지 않은 것)
    public List<Friendship> checkMemberReceiveNotAccept(FriendshipCheckRequest request){
        Member member = memberRepository.findByEmail(request.getMemberEmail()).orElseThrow(MemberNotFoundException::new);
        List<Friendship> friendshipList = friendshipRepository.findPendingFrendshipRequestedByMemberId(member.getId());
        return friendshipList;
    }

    @Transactional
    // 친구 추가 거절 or 수락 요청 처리 및 이후 친구 삭제 기능 시 사용 가능
    public int updateFriendship(FriendshipUpdateRequest request){
        int updatedRows; // 변환된 행 개수 반환 받음
        Member requester = memberRepository.findByEmail(request.getRequesterEmail()).orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findByEmail(request.getReceiverEmail())
                .orElseThrow(MemberNotFoundException::new);
        validateFriendshipExist(requester, receiver);

        Optional<Friendship> friendship = friendshipRepository.findByRequesterIdAndReceiverId(requester.getId(), receiver.getId());

        if(request.getIsAccepted()){
            friendship.get().setAccepted(true);
            updatedRows = 1;
        }
        else {
            updatedRows = friendshipRepository.deleteByRequesterIdAndReceiverId(requester.getId(), receiver.getId());
        }
        return updatedRows;
    }

    // 친구 추가 요청시 중복된 friendship이 이미 존재하는지 확인
    private void validateDuplicateFriendship(Member Requester, Member Receiver){
        friendshipRepository.findByRequesterIdAndReceiverId(Requester.getId(), Receiver.getId())
                        .ifPresent(friendship -> {
                            throw new RuntimeException("이미 신청되었거나 친구입니다.");
                        });

        friendshipRepository.findByRequesterIdAndReceiverId(Receiver.getId(), Requester.getId())
                        .ifPresent(friendship -> {
                            throw new RuntimeException("이미 신청되었거나 친구입니다.");
                        });
    }

    // checkAccept, updateFriendship 함수 사용 전 실제로 해당 요청이 있었는지 확인
    private void validateFriendshipExist(Member Requester, Member Receiver){
        if(friendshipRepository.findByRequesterIdAndReceiverId(Requester.getId(), Receiver.getId()).isEmpty())
            throw new RuntimeException("목표 friendship이 존재하지 않습니다.");
    }
}
