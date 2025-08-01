package com.grepp.spring.app.model.team.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TeamDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

}
