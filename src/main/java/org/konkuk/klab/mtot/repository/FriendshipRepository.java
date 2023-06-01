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

    Optional<Friendship> findByRequesterIdAndReceiverId(@Param("memberId") Long requesterId, @Param("memberId") Long receiverId);

    // 유저가 처리하지 않은 않은 친구 요청을 반환함. (수락 혹은 거절되지 않은)
    @Query("select f from Friendship f where f.receiver.id=:memberId and f.isAccepted=false")
    List<Friendship> findPendingFriendshipReceivedByMemberId(@Param("memberId") Long memberId);

    // 유저가 보낸 요청 중 아직 처리되지 않은 요청을 반환함. (수락 혹은 거절되지 않은)
    @Query("select f from Friendship f where f.requester.id=:memberId and f.isAccepted=false")
    List<Friendship> findPendingFriendshipRequestedByMemberId(@Param("memberId") Long memberId);

    // 유저와 친구 관계인 것들 반환 (수락된 혹은 수락한 요청들)
    @Query("select f from Friendship f where (f.requester.id=:memberId or f.receiver.id=:memberId) and f.isAccepted=true")
    List<Friendship> findMemberFriend(@Param("memberId") Long memberId);

    @Transactional
    @Modifying
    @Query("delete from Friendship f where f.requester.id=:requesterId and f.receiver.id=:receiverId")
    int deleteByRequesterIdAndReceiverId(@Param("requesterId") Long requesterId, @Param("receiverId") Long receiverId);
}

