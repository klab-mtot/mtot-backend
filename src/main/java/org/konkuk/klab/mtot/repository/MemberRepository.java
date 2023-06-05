package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query("select m from Member m " +
            "join MemberTeam mt " +
            "on m.id=mt.member.id " +
            "where mt.team.id=:teamId")
    List<Member> getAllTeamMemberByTeamId(@Param("teamId") Long teamId);
}
