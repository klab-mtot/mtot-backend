package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    @Query("select mg from MemberTeam mg where mg.member.id =:memberId")
    List<MemberTeam> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("select mg from MemberTeam mg where mg.member.email =:memberEmail")
    List<MemberTeam> findAllByMemberEmail(@Param("memberEmail") String memberEmail);

    Optional<MemberTeam> findByMemberIdAndTeamId(Long memberId, Long teamId);
}
