package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PinRepository extends JpaRepository<Pin, Long> {

    @Query("select p from Pin p " +
            "where p.member.id =:memberId and " +
            "p.journey.id =:journeyId " +
            "order by p.createdTime desc " +
            "limit 1")
    Optional<Pin> findFirstPinByMemberIdAndJourneyId(@Param("memberId") Long memberId,
                                                     @Param("journeyId") Long journeyId);

    List<Pin> findByJourneyId(@Param("journeyId") Long journeyId);
}
