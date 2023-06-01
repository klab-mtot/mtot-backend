package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
