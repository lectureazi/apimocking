package com.grepp.spring.app.model.team.repos;

import com.grepp.spring.app.model.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeamRepository extends JpaRepository<Team, Long> {
}
