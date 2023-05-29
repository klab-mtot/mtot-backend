package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Query("SELECT p FROM Photo p WHERE p.journey.id = :journeyId")
    Optional<Photo> findByJourneyId(@Param("journeyId") Long journeyId);
}