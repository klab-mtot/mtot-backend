package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship> findByRequesterIdAndReceiverId(Long requesterId, Long receiverId);

    @Transactional
    int deleteByRequesterIdAndReceiverId(Long requesterId, Long receiverId);
}

