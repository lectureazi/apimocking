package com.grepp.spring.app.model.task.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskDTO {

    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    private OffsetDateTime startAt;

    private OffsetDateTime endAt;

    @NotNull
    private Long schedule;

}
