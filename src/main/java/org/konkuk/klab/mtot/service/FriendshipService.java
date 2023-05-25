package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Friendship;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.dto.request.FriendshipRequest;
import org.konkuk.klab.mtot.dto.request.FriendshipUpdateRequest;
import org.konkuk.klab.mtot.dto.response.FriendshipResponse;
import org.konkuk.klab.mtot.repository.FriendshipRepository;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public FriendshipResponse requestFriend(FriendshipRequest request){
        List<Member> memberList = findMemberIds(request.getRequesterEmail(), request.getReceiverEmail());
        Member requester = memberList.get(0);
        Member receiver = memberList.get(1);

        validateDuplicateFriendship(requester, receiver);

        Friendship friendship = new Friendship(requester, receiver);
        return new FriendshipResponse(friendshipRepository.save(friendship).getId());
    }

    @Transactional(readOnly = true)
    private List<Member> findMemberIds(String requesterEmail, String receiverEmail) {
        Optional<Member> requester = memberRepository.findByEmail(requesterEmail);
        Optional<Member> receiver = memberRepository.findByEmail(receiverEmail);

        if(requester.isEmpty()) {throw new RuntimeException("Requester 멤버가 존재하지 않습니다.");}
        if(receiver.isEmpty()) {throw new RuntimeException("Receiver 멤버가 존재하지 않습니다.");}

        List<Member> memberList = new ArrayList<>();
        memberList.add(requester.get());
        memberList.add(receiver.get());

        return memberList;
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

    @Transactional
    // 친구 추가 거절 or 수락 요청 처리 및 이후 친구 삭제 기능 시 사용 가능
    public int updateFriendship(FriendshipUpdateRequest request){
        int updatedRows; // 변환된 행 개수 반환 받음
        List<Member> memberList = findMemberIds(request.getRequesterEmail(), request.getReceiverEmail());
        Member requester = memberList.get(0);
        Member receiver = memberList.get(1);
        validateFriendshipExist(requester, receiver);

        Optional<Friendship> friendship = friendshipRepository.findByRequesterIdAndReceiverId(requester.getId(), receiver.getId());

        if(request.getAccept()){
            updatedRows = friendshipRepository.updateAcceptTrue(requester.getId(), receiver.getId());
        }
        else {
            updatedRows = friendshipRepository.deleteByRequesterIdAndReceiverId(requester.getId(), receiver.getId());
        }
        return updatedRows;
    }

    // checkAccept, updateFriendship 함수 사용 전 실제로 해당 요청이 있었는지 확인
    private void validateFriendshipExist(Member Requester, Member Receiver){
        if(friendshipRepository.findByRequesterIdAndReceiverId(Requester.getId(), Receiver.getId()).isEmpty())
            throw new RuntimeException("목표 friendship이 존재하지 않습니다.");
    }
}
