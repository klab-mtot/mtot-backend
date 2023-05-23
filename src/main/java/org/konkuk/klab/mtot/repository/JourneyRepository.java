package org.konkuk.klab.mtot.repository;
import org.konkuk.klab.mtot.domain.Journey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JourneyRepository extends JpaRepository<Journey, Long> {
    Optional<Journey> findById(Long id);
}
