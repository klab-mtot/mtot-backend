package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.MemberTeam;
import org.konkuk.klab.mtot.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    @Query("select mg from MemberTeam mg where mg.member.id =:memberId")
    List<MemberTeam> findAllByMemberId(Long memberId);

    Optional<MemberTeam> findByMemberIdAndTeamId(Long memberId, Long teamId);
}
