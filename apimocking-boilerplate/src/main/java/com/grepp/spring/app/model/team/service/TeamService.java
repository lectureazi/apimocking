package com.grepp.spring.app.model.team.service;

import com.grepp.spring.app.model.team.domain.Team;
import com.grepp.spring.app.model.team.model.TeamDTO;
import com.grepp.spring.app.model.team.repos.TeamRepository;
import com.grepp.spring.app.model.team_member.domain.TeamMember;
import com.grepp.spring.app.model.team_member.repos.TeamMemberRepository;
import com.grepp.spring.app.model.team.domain.Team;
import com.grepp.spring.app.model.team.model.TeamDTO;
import com.grepp.spring.app.model.team.repos.TeamRepository;
import com.grepp.spring.app.model.team_member.domain.TeamMember;
import com.grepp.spring.app.model.team_member.repos.TeamMemberRepository;
import com.grepp.spring.infra.error.exceptions.CommonException;
import com.grepp.spring.infra.response.ResponseCode;
import com.grepp.spring.util.NotFoundException;
import com.grepp.spring.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    public TeamService(final TeamRepository teamRepository,
            final TeamMemberRepository teamMemberRepository) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    public List<TeamDTO> findAll() {
        final List<Team> teams = teamRepository.findAll(Sort.by("id"));
        return teams.stream()
                .map(team -> mapToDTO(team, new TeamDTO()))
                .toList();
    }

    public TeamDTO get(final Long id) {
        return teamRepository.findById(id)
                .map(team -> mapToDTO(team, new TeamDTO()))
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
    }

    public Long create(final TeamDTO teamDTO) {
        final Team team = new Team();
        mapToEntity(teamDTO, team);
        return teamRepository.save(team).getId();
    }

    public void update(final Long id, final TeamDTO teamDTO) {
        final Team team = teamRepository.findById(id)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        mapToEntity(teamDTO, team);
        teamRepository.save(team);
    }

    public void delete(final Long id) {
        teamRepository.deleteById(id);
    }

    private TeamDTO mapToDTO(final Team team, final TeamDTO teamDTO) {
        teamDTO.setId(team.getId());
        teamDTO.setName(team.getName());
        return teamDTO;
    }

    private Team mapToEntity(final TeamDTO teamDTO, final Team team) {
        team.setName(teamDTO.getName());
        return team;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Team team = teamRepository.findById(id)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        final TeamMember teamTeamMember = teamMemberRepository.findFirstByTeam(team);
        if (teamTeamMember != null) {
            referencedWarning.setKey("team.teamMember.team.referenced");
            referencedWarning.addParam(teamTeamMember.getId());
            return referencedWarning;
        }
        return null;
    }

}
