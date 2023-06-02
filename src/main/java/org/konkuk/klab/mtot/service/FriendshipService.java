package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Friendship;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.dto.response.*;
import org.konkuk.klab.mtot.exception.*;
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
    public SendFriendshipResponse requestFriend(String requesterEmail, String receiverEmail){
        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findByEmail(receiverEmail)
                .orElseThrow(MemberNotFoundException::new);
        validateDuplicateFriendship(requester, receiver);

        Friendship friendship = new Friendship(requester, receiver);
        return new SendFriendshipResponse(friendshipRepository.save(friendship).getId());
    }

    @Transactional(readOnly = true)
    // 유저 친구 확인
    public GetAllFriendshipInfoResponse findFriendshipList(String memberEmail){
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(MemberNotFoundException::new);
        List<FriendshipInfoResponse> friendshipInfoResponses = friendshipRepository.findFriendsByMemberId(member.getId())
                .stream()
                .map(friendship -> {
                    Member requester = friendship.getRequester();
                    Member receiver = friendship.getReceiver();
                    if (requester.getId().equals(member.getId()))
                        return new FriendshipInfoResponse(friendship.getId(), receiver.getId(), receiver.getName(), receiver.getEmail());
                    else return new FriendshipInfoResponse(friendship.getId(), requester.getId(), requester.getName(), requester.getEmail());
                    
                })
                .toList();
        return new GetAllFriendshipInfoResponse(friendshipInfoResponses);
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
    public GetAllPendingFriendshipResponse getAllPendingFriendRequests(String memberEmail){

        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(MemberNotFoundException::new);

        List<PendingFriendshipResponse> pendingFriendshipResponses =
                friendshipRepository.findPendingFriendshipReceivedByMemberId(member.getId())
                        .stream()
                        .map(friendship -> {
                            return new PendingFriendshipResponse(
                                    friendship.getId(),
                                    friendship.getRequester().getName(),
                                    friendship.getRequester().getEmail());
                        })
                        .toList();
        return new GetAllPendingFriendshipResponse(pendingFriendshipResponses);
    }

    @Transactional
    // 친구 추가 거절 or 수락 요청 처리 및 이후 친구 삭제 기능 시 사용 가능
    public FriendshipUpdateResponse updateFriendship(String email, boolean isAccepted, Long friendShipId){

        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        Friendship friendship = friendshipRepository.findById(friendShipId)
                .orElseThrow(FriendshipNotFoundException::new);

        // TEST 필요
        if (!friendship.getReceiver().getId().equals(member.getId())) throw new IllegalFriendshipAccessException();

        if (isAccepted) friendship.setAccepted(true);
        else friendshipRepository.deleteByFriendshipId(friendShipId);

        return new FriendshipUpdateResponse(true);
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
