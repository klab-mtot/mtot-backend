package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.Photo;
import org.konkuk.klab.mtot.domain.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query("select pt from Photo pt where pt.pin.id =:pinId")
    List<Photo> findByPin(Pin pin);

    @Query("select pt from Photo pt where pt.pin.journey.id =:journeyId")
    List<Photo> findByJourney(Journey journey);
}
