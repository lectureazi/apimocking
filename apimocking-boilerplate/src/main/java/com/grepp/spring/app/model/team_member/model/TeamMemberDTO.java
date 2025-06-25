package com.grepp.spring.app.model.team_member.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TeamMemberDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String username;

    @NotNull
    private Long team;

}
