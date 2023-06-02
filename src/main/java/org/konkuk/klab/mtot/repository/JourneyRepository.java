package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JourneyRepository extends JpaRepository<Journey, Long> {
    @Query("select j from Journey j where j.id =: journeyId")
    Optional<Journey> findById(@Param("journeyId")Long journeyId);

}
