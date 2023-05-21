package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Friendship;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.dto.request.FriendshipRequest;
import org.konkuk.klab.mtot.dto.response.FriendshipResponse;
import org.konkuk.klab.mtot.repository.FriendshipRepository;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public FriendshipResponse requestFriend(FriendshipRequest request){
        Optional<Member> requester = memberRepository.findByEmail(request.getRequesterEmail());
        Optional<Member> receiver = memberRepository.findByEmail(request.getReceiverEmail());

        if(requester.isEmpty()) {throw new RuntimeException("Requester 멤버가 존재하지 않습니다.");}
        if(receiver.isEmpty()) {throw new RuntimeException("Receiver 멤버가 존재하지 않습니다.");}
        validateDuplicateFriendship(requester, receiver);

        Friendship friendship = new Friendship(requester.get(), receiver.get());
        return new FriendshipResponse(friendshipRepository.save(friendship).getId());
    }

    private void validateDuplicateFriendship(Optional<Member> Requester, Optional<Member> Receiver){
        friendshipRepository.findByRequesterIdAndReceiverId(Requester.get().getId(), Receiver.get().getId())
                        .ifPresent(friendship -> {
                            throw new RuntimeException("이미 친구입니다.");
                        });

        friendshipRepository.findByRequesterIdAndReceiverId(Receiver.get().getId(), Requester.get().getId())
                        .ifPresent(friendship -> {
                            throw new RuntimeException("이미 친구입니다.");
                        });
    }
}
