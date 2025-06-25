package com.grepp.spring.app.model.team_member.service;

import com.grepp.spring.app.model.team.repos.TeamRepository;
import com.grepp.spring.app.model.team_member.domain.TeamMember;
import com.grepp.spring.app.model.team.domain.Team;
import com.grepp.spring.app.model.team.repos.TeamRepository;
import com.grepp.spring.app.model.team_member.domain.TeamMember;
import com.grepp.spring.app.model.team_member.model.TeamMemberDTO;
import com.grepp.spring.app.model.team_member.repos.TeamMemberRepository;
import com.grepp.spring.infra.error.exceptions.CommonException;
import com.grepp.spring.infra.response.ResponseCode;
import com.grepp.spring.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;

    public TeamMemberService(final TeamMemberRepository teamMemberRepository,
            final TeamRepository teamRepository) {
        this.teamMemberRepository = teamMemberRepository;
        this.teamRepository = teamRepository;
    }

    public List<TeamMemberDTO> findAll() {
        final List<TeamMember> teamMembers = teamMemberRepository.findAll(Sort.by("id"));
        return teamMembers.stream()
                .map(teamMember -> mapToDTO(teamMember, new TeamMemberDTO()))
                .toList();
    }

    public TeamMemberDTO get(final Long id) {
        return teamMemberRepository.findById(id)
                .map(teamMember -> mapToDTO(teamMember, new TeamMemberDTO()))
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
    }

    public Long create(final TeamMemberDTO teamMemberDTO) {
        final TeamMember teamMember = new TeamMember();
        mapToEntity(teamMemberDTO, teamMember);
        return teamMemberRepository.save(teamMember).getId();
    }

    public void update(final Long id, final TeamMemberDTO teamMemberDTO) {
        final TeamMember teamMember = teamMemberRepository.findById(id)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        mapToEntity(teamMemberDTO, teamMember);
        teamMemberRepository.save(teamMember);
    }

    public void delete(final Long id) {
        teamMemberRepository.deleteById(id);
    }

    private TeamMemberDTO mapToDTO(final TeamMember teamMember, final TeamMemberDTO teamMemberDTO) {
        teamMemberDTO.setId(teamMember.getId());
        teamMemberDTO.setUsername(teamMember.getUsername());
        teamMemberDTO.setTeam(teamMember.getTeam() == null ? null : teamMember.getTeam().getId());
        return teamMemberDTO;
    }

    private TeamMember mapToEntity(final TeamMemberDTO teamMemberDTO, final TeamMember teamMember) {
        teamMember.setUsername(teamMemberDTO.getUsername());
        final Team team = teamMemberDTO.getTeam() == null ? null : teamRepository.findById(teamMemberDTO.getTeam())
                .orElseThrow(() -> new NotFoundException("team not found"));
        teamMember.setTeam(team);
        return teamMember;
    }

}
