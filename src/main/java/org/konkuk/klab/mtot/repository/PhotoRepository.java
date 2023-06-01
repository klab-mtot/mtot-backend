package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Photo;
import org.konkuk.klab.mtot.domain.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query("select pt from Photo pt where pt.pin.id =:pinId")
    List<Photo> findByPin(Pin pin);

    @Query("select pt from Photo pt where pt.pin.journey.id =:journeyId")
    List<Photo> findByJourney(Journey journey);

    @Query("select pt from Photo pt where pt.uploadDate >=: date1 and pt.uploadDate <=: date2")
    List<Photo> findByMonth(@Param("startDate") LocalDate date1, @Param("endDate") LocalDate date2);
}
