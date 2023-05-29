package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.journey.id =:journeyId")
    Optional<Post> findByJourneyId(@Param("journeyId") Long journeyId);
}
