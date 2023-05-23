package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Transactional
    Optional<Friendship> findByRequesterIdAndReceiverId(Long requesterId, Long receiverId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Friendship f set f.accept=true where f.requester.id=:requesterId and f.receiver.id=:receiverId")
    int updateAcceptTrue(Long requesterId, Long receiverId);
    @Transactional
    int deleteByRequesterIdAndReceiverId(Long requesterId, Long receiverId);
}

