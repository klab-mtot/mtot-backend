package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship> findByRequesterIdAndReceiverId(Long requesterId, Long receiverId);

    // 유저가 받지 않은 친구 요청을 발환함
    @Query("select f from Friendship f where f.receiver.id=:memberId and f.isAccepted=false")
    List<Friendship> findPendingFriendshipReceivedByMemberId(Long memberId);

    @Query("select f from Friendship f where f.requester.id=:memberId and f.isAccepted=false")
    List<Friendship> findPendingFrendshipRequestedByMemberId(Long memberId);

    @Query("select f from Friendship f where f.receiver.id=:memberId and f.isAccepted=false")
    List<Friendship> findMemberFriend(Long memberId);

    @Transactional
    @Modifying
    @Query("delete from Friendship f where f.requester.id=:requesterId and f.receiver.id=:receiverId")
    int deleteByRequesterIdAndReceiverId(@Param("requesterId") Long requesterId, @Param("receiverId") Long receiverId);
}

