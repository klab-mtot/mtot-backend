package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Query(
            "select pp from Photo pp where pp.id in " +
            "(select min(p.id) " +
            "from Photo p " +
            "join p.pin pin " +
            "join pin.journey j " +
            "where j.id in (select j.id from Journey j join j.team t join t.memberTeams mt where mt.member.id =:memberId) " + // 멤버가 속한 팀의 여정의 사진 전체
            "and p.uploadDate between :start and :end " +
            "group by p.uploadDate)"
            )
    List<Photo> getThumbnailPhotosBetween(@Param("memberId") Long memberId,
                                          @Param("start") LocalDate start,
                                          @Param("end") LocalDate end);

    @Query("select p from Photo p where p.pin.id =:pinId")
    List<Photo> findAllByPinId(@Param("pinId") Long pinId);

    @Query("select p from Photo p where p.pin.journey.id =:journeyId")
    List<Photo> findByJourneyId(@Param("journeyId") Long journeyId);
}
