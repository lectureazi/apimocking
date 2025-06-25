package com.grepp.spring.app.model.team_member.repos;

import com.grepp.spring.app.model.team_member.domain.TeamMember;
import com.grepp.spring.app.model.team.domain.Team;
import com.grepp.spring.app.model.team_member.domain.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    TeamMember findFirstByTeam(Team team);

}
