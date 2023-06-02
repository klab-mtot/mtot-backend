package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

    @Query("select journey from Journey journey join journey.team team join team.memberTeams memberTeam where memberTeam.member.id =:memberId")
    List<Journey> getJourneysFromMember(@Param("memberId") Long memberId);

}
