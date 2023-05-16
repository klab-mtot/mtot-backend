package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
